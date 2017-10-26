package com.parser.process;

import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.parser.dto.JobParameter.*;
import com.parser.entity.BannedIpEntity;
import com.parser.entity.IpAggregatedEntity;

@Component
@StepScope
public class BannedIpItemProcessor implements ItemProcessor<IpAggregatedEntity, BannedIpEntity> {

	private static final String REASON_MESSAGE = "%s send request %d times that exceeds max allowed limit %d";
	
	@Value("#{jobParameters}") 
	private Map<String, ?> jobParameters;
	
	private Long jobId;
	
	@BeforeStep
	public void setInterStepData(StepExecution stepExecution) {
	    JobExecution jobExecution = stepExecution.getJobExecution();
	    this.jobId = jobExecution.getJobId();
	}
	
	@Override
	public BannedIpEntity process(IpAggregatedEntity item) throws Exception {
		BannedIpEntity entity = new BannedIpEntity();
		
		entity.setIp(item.getIp());
		entity.setJobId(jobId);
		entity.setReason(String.format(REASON_MESSAGE, 
				item.getIp(), 
				item.getTotal(), 
				jobParameters.get(threshold.name())));
		
		return entity;
	}
}