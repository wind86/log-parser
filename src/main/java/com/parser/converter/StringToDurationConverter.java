package com.parser.converter;

import static org.apache.commons.lang3.EnumUtils.getEnum;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.upperCase;

import org.springframework.core.convert.converter.Converter;

import com.parser.dto.Duration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToDurationConverter implements Converter<String, Duration> {
	
	@Override
	public Duration convert(String source) {
		if (isBlank(source)) {
			log.warn("duration is blank");
			return null;
		}
		
		return getEnum(Duration.class, upperCase(source));
	}
}