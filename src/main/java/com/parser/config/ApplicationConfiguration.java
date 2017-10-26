package com.parser.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.parser.converter.StringToDateConverter;
import com.parser.converter.StringToDurationConverter;
import com.parser.dto.Duration;
import com.parser.factory.DailyDurationFactory;
import com.parser.factory.DurationPeriodFactory;
import com.parser.factory.HourlyDurationFactory;

@Profile("dev")
@Configuration
public class ApplicationConfiguration {
	
	@Bean
	public ConversionService conversionService() {
		DefaultConversionService service = new DefaultConversionService();
		service.addConverter(new StringToDateConverter());
		service.addConverter(new StringToDurationConverter());
		return service;
	}
	
	@Bean
	// TODO: check if parameter constants can be read from Parameter enum
	public DurationPeriodFactory durationPeriodFactory(@Value("${duration}") Duration duration, @Value("${startDate}") Date date) {
		switch (duration) {
		case HOURLY:
			return new HourlyDurationFactory(date);
		case DAILY:
			return new DailyDurationFactory(date);
		default:
			throw new IllegalArgumentException("unsupported duration");
		}
	}
/*
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("taskExecutor");
		asyncTaskExecutor.setConcurrencyLimit(50);
		return asyncTaskExecutor;
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setMaxPoolSize(50);
//		taskExecutor.afterPropertiesSet();
//		return taskExecutor;
	}
*/
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}