package com.parser.listener;

import static org.springframework.batch.core.BatchStatus.*;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobCompletionListener extends JobExecutionListenerSupport {

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (COMPLETED == jobExecution.getStatus()) {
			log.info("!!! JOB FINISHED!");
		} else if (FAILED == jobExecution.getStatus()) {
			log.error("!!! JOB FAILED!");
		}
	}
}