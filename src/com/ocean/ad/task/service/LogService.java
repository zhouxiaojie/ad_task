package com.ocean.ad.task.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocean.ad.task.dao.ILogEventCountDao;
import com.ocean.ad.task.dao.ILogEventDao;
import com.ocean.ad.task.model.LogEventCount;
import com.ocean.util.DateUtil;
import com.ocean.util.JsonUtils;

@Service("logService")
public class LogService implements ILogService{
	@Resource
	private ILogEventCountDao logEventCountDao;
	@Resource
	private ILogEventDao logEventDao;	
	@Resource
	private ILogService logService;
	private String startTime="2015-06-25 00:00";
	private String [] events =new String[]{"reqad_s","reqad_f","show_s","init_s","init_f","dau"};
	private int before=0;
	private String log_event_table="log_event_";
	private String log_event_count_table="log_event_count_";
	private String job="log_count";
	@Override
	public void logJob(){
		Object content = null;
		Date now = new Date();
		try {
			for (String event : events) {
				String year = DateUtil.DateYYYYFmt(now);
				String startMin = logEventCountDao.selectMaxTimeByEvent(log_event_count_table+year, event);
				if(startMin==null){
					if("2015".equals(year))
						startMin=startTime;
					else{
						Integer yearPre = Integer.parseInt(year)-1;
						startMin = logEventCountDao.selectMaxTimeByEvent(log_event_count_table+yearPre, event);
						if((yearPre+"-12-31 23:59").equals(startMin)){
							startMin=year+"-01-01 00:00";
						}
						
					}
				}
				Date maxDate = DateUtil.parseMinFmt(startMin);
				Calendar ca = Calendar.getInstance();
				ca.setTime(maxDate);
				
				for(int i=0;i<getDateRangeMin(maxDate.getTime(),now.getTime())-before;i++){
					ca.add(Calendar.MINUTE,1);
					String minDate = DateUtil.DateMinFmt(ca.getTime());
					String date = DateUtil.DateYYMMDDFmt(ca.getTime());
					int count = logEventDao.selectCountOnMin(log_event_table+date, event,minDate);
					int uvCount = logEventDao.selectCountOnMin(log_event_table+date, event,minDate);
					LogEventCount lec = new LogEventCount(event, count, uvCount, minDate,now,log_event_count_table+DateUtil.DateYYYYFmt(ca.getTime()));
					content = lec;
					logEventCountDao.insert(lec);
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				if(content!=null)
					logEventCountDao.insertJobFailed(job,JsonUtils.OtoJson(content), now);
				content=null;
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
	}
	
	private int getDateRangeMin(long start,long end){
		return new Long((end- start)/(60*1000)).intValue();
	}
}
