package com.parser.query;

public interface Queries {

	String INSERT_LOG_ITEM_SQL = "INSERT INTO log_item(job_id, created_at,ip,request,status,user_agent) "
			+ "VALUES (:jobId, :createdAt,:ip,:request,:status,:userAgent)";
	
	String AGGREGATE_IP_CALLS_SQL = 
			"select " + 
			"	ip, " +
			"	count(id) as total " +
			"from log_item " +
			"where created_at between ? and ? " +
			"and job_id = ? " +
			"group by ip " +
			"having count(id) > ?";
	
	String INSERT_BANNED_IP_SQL = "insert into banned_ips(job_id, ip, reason) values (:jobId, :ip, :reason)";

}