package com.ocean.ad.task.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

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
	private String [] events =new String[]{"reqad_s","reqad_f","show_s","show_f","init_s","init_f","click"};
	private int before=1;
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
					if("2015".equals(year)){
						Calendar ca = Calendar.getInstance();
						ca.setTime(now);
						ca.set(Calendar.HOUR_OF_DAY,0);
						ca.set(Calendar.MINUTE,0);
						startMin=DateUtil.DateMinFmt(ca.getTime());						
					}
					else{
						Integer yearPre = Integer.parseInt(year)-1;
						startMin = logEventCountDao.selectMaxTimeByEvent(log_event_count_table+yearPre, event);
						if((yearPre+"-12-31 23:59").equals(startMin)){
							startMin=year+"-01-01 00:00";
						}
						
					}
				}
				Date startDate = DateUtil.parseMinFmt(startMin);
				Calendar ca = Calendar.getInstance();
				ca.setTime(startDate);
				for(int i=0;i<getDateRangeMin(startDate.getTime(),now.getTime())-before;i++){
					ca.add(Calendar.MINUTE,1);
					String minDate = DateUtil.DateMinFmt(ca.getTime());
					String date = DateUtil.DateYYMMDDFmt(ca.getTime());
					int count = logEventDao.selectCountOnMin(log_event_table+date, event,minDate);
					int uvCount = logEventDao.selectCountUvOnMin(log_event_table+date, event,minDate);
					LogEventCount lec = new LogEventCount(event, count, uvCount, minDate,now,log_event_count_table+DateUtil.DateYYYYFmt(ca.getTime()));
					content = lec;
					try {						
						logEventCountDao.insert(lec);
					} catch (Exception e) {
						// TODO: handle exception
						logEventCountDao.insertJobFailed(job,JsonUtils.OtoJson(content), now);
					}
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			
		}
	}
	
	private int getDateRangeMin(long start,long end){
		return new Long((end- start)/(60*1000)).intValue();
	}
}
