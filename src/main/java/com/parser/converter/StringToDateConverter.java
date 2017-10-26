package com.parser.converter;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

import java.text.ParseException;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToDateConverter implements Converter<String, Date> {

	private static final String DATE_PATTERN = "yyyy-MM-dd.HH:mm:ss";
	
	@Override
	public Date convert(String source) {
		if (isBlank(source)) {
			log.warn("date is blank");
			return null;
		}
		
		try {
			return parseDate(trim(source), DATE_PATTERN);
		} catch (ParseException e) {
			log.error("unable to parse date: {}", source);
		}
		
		return null;
	}
}