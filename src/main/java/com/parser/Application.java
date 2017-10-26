package com.parser;

import static com.parser.dto.JobParameter.fileName;
import static com.parser.dto.JobParameter.fromDate;
import static com.parser.dto.JobParameter.threshold;
import static com.parser.dto.JobParameter.tillDate;
import static java.util.Arrays.stream;
import static org.springframework.boot.SpringApplication.run;

import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.parser.factory.DurationPeriodFactory;

@SpringBootApplication
@EnableBatchProcessing
public class Application {
	
	public static void main(String[] args) throws Exception {
		Map<Parameter, String> params = readCommandLineParameters(args);
		
		ApplicationContext context = run(Application.class, args);
		DurationPeriodFactory durationPeriodFactory = context.getBean(DurationPeriodFactory.class);
		
		JobParameters jobParameters = createJobParameters(durationPeriodFactory, params);
		
		JobLauncher jobLauncher = context.getBean(JobLauncher.class);
		jobLauncher.run(context.getBean(Job.class), jobParameters);
	}
	
	private static Map<Parameter, String> readCommandLineParameters(String[] args) throws ParseException {
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(createCommandLineOptions(), args);
		
		Map<Parameter, String> params = new EnumMap<>(Parameter.class);
		stream(Parameter.values())
			.forEach(param -> params.put(param, line.getOptionValue(param.name())));
		
		return params;
	}
	
	private static Options createCommandLineOptions() {
		Options options = new Options();
		
		stream(Parameter.values()).forEach(param -> 
			options.addOption(Option.builder().longOpt(param.name()).hasArg().build())
		);
		
		return options;
	}
	
	private static JobParameters createJobParameters(DurationPeriodFactory durationPeriodFactory, Map<Parameter, String> params) {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		
		jobParametersBuilder.addDate(fromDate.name(), durationPeriodFactory.createFromDate());
		jobParametersBuilder.addDate(tillDate.name(), durationPeriodFactory.createTillDate());
		jobParametersBuilder.addLong(threshold.name(), Long.valueOf(params.get(Parameter.threshold)));
		jobParametersBuilder.addString(fileName.name(), params.get(Parameter.accesslog));
		// just make different set of input parameters to allow job executing more than once
		jobParametersBuilder.addLong("time", System.currentTimeMillis());
	
		return jobParametersBuilder.toJobParameters();
	}
}