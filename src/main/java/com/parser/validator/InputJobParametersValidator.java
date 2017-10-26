package com.parser.validator;

import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class InputJobParametersValidator implements JobParametersValidator {
	
	private static final String INVALID_PARAMETER_MESSAGE = "'%s' is not specified";

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		Map<String, JobParameter> parameterMap = parameters.getParameters();

		for (Map.Entry<String, JobParameter> entry : parameterMap.entrySet()) {
			if (entry.getValue() == null) {
				throw new JobParametersInvalidException(String.format(INVALID_PARAMETER_MESSAGE, entry.getKey()));
			}
		}
	}
}