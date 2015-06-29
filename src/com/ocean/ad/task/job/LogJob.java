package com.ocean.ad.task.job;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import com.ocean.ad.task.dao.ILogEventCountDao;
import com.ocean.ad.task.dao.ILogEventDao;
import com.ocean.ad.task.model.LogEventCount;
import com.ocean.ad.task.service.ILogService;
import com.ocean.util.DateUtil;
import com.ocean.util.JsonUtils;

public class LogJob {
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
	private int threadCount=0;
	
	protected void execute( ) {
		// TODO Auto-generated method stub
		if(threadCount>0){
			return;
		}
		synchronized (this) {			
			threadCount++;
		}
		logService.logJob();
		synchronized (this) {			
				threadCount--;
		}
		
		
	}
	
	private int getDateRangeMin(long start,long end){
		return new Long((end- start)/(60*1000)).intValue();
	}

}
