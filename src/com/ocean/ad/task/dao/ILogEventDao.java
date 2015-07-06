package com.ocean.ad.task.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ILogEventDao {
	@Select("SELECT count(*) FROM ${tableName} where "
			+ "event =#{event} and min_date = #{currMin};")
	public int selectCountOnMin(@Param("tableName")String tableName,@Param("event")String event,@Param("currMin")String currMin);
	
	
	@Select("SELECT count(distinct imei,mac) FROM ${tableName} where "
			+ "event =#{event} and min_date = #{currMin}")
	public int selectCountUvOnMin(@Param("tableName")String tableName,@Param("event")String event,@Param("currMin")String currMin);
}
