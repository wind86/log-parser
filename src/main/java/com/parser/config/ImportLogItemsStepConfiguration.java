package com.parser.config;

import static com.parser.query.Queries.*;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFieldsList;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;

import com.parser.dto.JobParameter;
import com.parser.dto.LogItemDto;
import com.parser.entity.LogItemEntity;
import com.parser.process.ImportLogItemProcessor;

@Configuration
public class ImportLogItemsStepConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

//	@Autowired
//	private TaskExecutor taskExecutor;
	
	@Autowired
	private FlatFileItemReader<LogItemDto> logFileReader;
	
	@Autowired
	private ImportLogItemProcessor importLogItemProcessor;
	
	private Long jobId;
	
	@Bean("importLogFileStep")
	public Step importLogFileStep() {
		return stepBuilderFactory.get("importLogFileStep")
				.<LogItemDto, LogItemEntity>chunk(500)
				.reader(logFileReader)
				.processor(importLogItemProcessor)
				.writer(logItemJdbcWriter())
//				.taskExecutor(taskExecutor)
				.build();
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<LogItemDto> logFileReader(@Value("#{jobParameters}") Map<String, ?> jobParameters) {
		DefaultLineMapper<LogItemDto> lineMapper = new DefaultLineMapper<>();
		
		lineMapper.setLineTokenizer(createLineTokenizer("|"));
		lineMapper.setFieldSetMapper(createFieldSetMapper());
		
		String fileName = (String) jobParameters.get(JobParameter.fileName.name());
		
		FlatFileItemReader<LogItemDto> fileReader = new FlatFileItemReader<>();

		fileReader.setResource(new FileSystemResource(fileName));
		fileReader.setLineMapper(lineMapper);
		
		return fileReader;
	}
	
	@Bean
	public JdbcBatchItemWriter<LogItemEntity> logItemJdbcWriter() {
		JdbcBatchItemWriter<LogItemEntity> logWriter = new JdbcBatchItemWriter<>();
		
		logWriter.setDataSource(dataSource);
		logWriter.setSql(INSERT_LOG_ITEM_SQL);
		logWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<LogItemEntity>());

		return logWriter;
	}

	private static LineTokenizer createLineTokenizer(String delimiter) {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(delimiter);
		
		List<String> fieldNames = getAllFieldsList(LogItemDto.class).stream()
				.map(Field::getName)
				.collect(toList());
		
		lineTokenizer.setNames(fieldNames.toArray(new String[fieldNames.size()]));
		
		return lineTokenizer;
	}

	private static FieldSetMapper<LogItemDto> createFieldSetMapper() {
		BeanWrapperFieldSetMapper<LogItemDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(LogItemDto.class);
		return fieldSetMapper;
	}
}