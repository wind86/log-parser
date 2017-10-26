package com.parser.process;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.parser.dto.LogItemDto;
import com.parser.entity.LogItemEntity;

@Component
public class ImportLogItemProcessor implements ItemProcessor<LogItemDto, LogItemEntity> {

	private static final String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss.SSS";

	private Long jobId;
	
	@BeforeStep
	public void setInterStepData(StepExecution stepExecution) {
	    JobExecution jobExecution = stepExecution.getJobExecution();
	    this.jobId = jobExecution.getJobId();
	}
	
	@Override
	public LogItemEntity process(LogItemDto itemDto) throws Exception {
		LogItemEntity entity = new LogItemEntity();

		entity.setJobId(jobId);
		entity.setCreatedAt(parseDate(itemDto.getDate(), DATE_PATTERN));
		entity.setIp(itemDto.getIp());
		entity.setRequest(itemDto.getRequest());
		entity.setStatus(itemDto.getStatus());
		entity.setUserAgent(itemDto.getUserAgent());

		return entity;
	}
}