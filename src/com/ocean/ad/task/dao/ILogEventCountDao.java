package com.ocean.ad.task.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ocean.ad.task.model.LogEventCount;

public interface ILogEventCountDao {
	@Select("select max(min_date) from ${tableName} where event = #{event};")
	public String selectMaxTimeByEvent(@Param("tableName")String tableName,@Param("event")String event);
	
	@Insert("insert into ${tableName} (event,count,uv_count,min_date,create_time) values ("
			+ "#{event},#{count},#{uvCount},#{minDate},#{createTime});")
	public void insert(LogEventCount lec);
	
	@Insert("insert into job_failed (job,content,create_time) values (#{job},#{content},#{createTime});")
	public void insertJobFailed(@Param("job")String job,@Param("content")String content,@Param("createTime")Date createTime);
}
