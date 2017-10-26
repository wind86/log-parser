package com.parser.reader;

import static com.parser.dto.JobParameter.fromDate;
import static com.parser.dto.JobParameter.threshold;
import static com.parser.dto.JobParameter.tillDate;
import static com.parser.query.Queries.AGGREGATE_IP_CALLS_SQL;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.parser.entity.IpAggregatedEntity;

@Component
@StepScope
public class AggregateIpItemJdbcReader extends JdbcCursorItemReader<IpAggregatedEntity> {

	@Autowired
	private DataSource dataSource;

	@Value("#{jobParameters}")
	private Map<String, ?> jobParameters;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		setSql(AGGREGATE_IP_CALLS_SQL);
		setRowMapper(new BeanPropertyRowMapper<>(IpAggregatedEntity.class));
	}

	@BeforeStep
	public void setInterStepData(StepExecution stepExecution) {
		JobExecution jobExecution = stepExecution.getJobExecution();
		setPreparedStatementSetter(pss -> {
			pss.setTimestamp(1, createTimestamp((Date) jobParameters.get(fromDate.name())));
			pss.setTimestamp(2, createTimestamp((Date) jobParameters.get(tillDate.name())));
			pss.setLong(3, jobExecution.getJobId());
			pss.setLong(4, (Long) jobParameters.get(threshold.name()));

		});
	}

	private static Timestamp createTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
}