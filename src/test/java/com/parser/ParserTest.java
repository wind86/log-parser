package com.parser;

import static com.parser.dto.JobParameter.fileName;
import static com.parser.dto.JobParameter.fromDate;
import static com.parser.dto.JobParameter.threshold;
import static com.parser.dto.JobParameter.tillDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.parser.Application;
import com.parser.config.ParserTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class, ParserTestConfiguration.class })
public class ParserTest {

	@Autowired
	private ApplicationContext context;

	@Test
	public void testEntireJob() throws Exception {
		JobParametersBuilder builder = new JobParametersBuilder();

		builder.addDate(fromDate.name(), new Date(1483275600000L));
		builder.addDate(tillDate.name(), new Date(1483279200000L));
		builder.addLong(threshold.name(), 5L);

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("test-access.log").getFile());

		builder.addString(fileName.name(), file.getAbsolutePath());

		JobLauncher launcher = context.getBean(JobLauncher.class);
		JobExecution result = launcher.run(context.getBean(Job.class), builder.toJobParameters());

		assertNotNull(result);
		assertEquals(BatchStatus.COMPLETED, result.getStatus());

		result.getStepExecutions().forEach(se -> assertEquals(BatchStatus.COMPLETED, se.getStatus()));
	}
}