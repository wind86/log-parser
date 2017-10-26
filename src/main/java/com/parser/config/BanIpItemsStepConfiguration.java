package com.parser.config;

import static com.parser.query.Queries.INSERT_BANNED_IP_SQL;
import static java.util.Arrays.asList;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import com.parser.entity.BannedIpEntity;
import com.parser.entity.IpAggregatedEntity;
import com.parser.process.BannedIpItemProcessor;
import com.parser.reader.AggregateIpItemJdbcReader;
import com.parser.writer.ConsoleItemWriter;

@Configuration
public class BanIpItemsStepConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

//	@Autowired
//	private TaskExecutor taskExecutor;
	
	@Autowired
	private BannedIpItemProcessor bannedIpItemProcessor;
	
	@Autowired
	private AggregateIpItemJdbcReader aggregateIpItemJdbcReader;
	
	@Bean("banIpItemStep")
	public Step banIpItemStep() {
		return stepBuilderFactory.get("banIpItemStep")
				.<IpAggregatedEntity, BannedIpEntity>chunk(50)
				.reader(aggregateIpItemJdbcReader)
				.processor(bannedIpItemProcessor)
				.writer(bannedIpItemWriters())
//				.taskExecutor(taskExecutor)
				.build();
	}

	@Bean
	public CompositeItemWriter<BannedIpEntity> bannedIpItemWriters() {
		CompositeItemWriter<BannedIpEntity> writer = new CompositeItemWriter<>();
		writer.setDelegates(asList(
				bannedIpItemJdbcWriter(), 
				new ConsoleItemWriter<BannedIpEntity>()
			));
		return writer;
	}
	
	@Bean
	public JdbcBatchItemWriter<BannedIpEntity> bannedIpItemJdbcWriter() {
		JdbcBatchItemWriter<BannedIpEntity> ipWriter = new JdbcBatchItemWriter<>();
		
		ipWriter.setSql(INSERT_BANNED_IP_SQL);
		ipWriter.setDataSource(dataSource);
		ipWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<BannedIpEntity>());
		
		return ipWriter;
	}
}