package com.parser.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.parser.listener.JobCompletionListener;
import com.parser.validator.InputJobParametersValidator;

@Configuration
public class JobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private JobCompletionListener jobCompletionListener;
	
	@Autowired
	private InputJobParametersValidator inputJobParametersValidator;

	@Autowired
	@Qualifier("importLogFileStep")
	private Step importLogFileStep;

	@Autowired
	@Qualifier("banIpItemStep")
	private Step banIpItemStep;

	@Bean
	public Job processLogFileJob() {
		return jobBuilderFactory.get("processLogFileJob")
				.incrementer(new RunIdIncrementer())
				.listener(jobCompletionListener)
				.validator(inputJobParametersValidator)
				.flow(importLogFileStep)
				.next(banIpItemStep)
				.end()
				.build();
	}
}