package com.parser.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleItemWriter<T> implements ItemWriter<T> {
	
	@Override
	public void write(List<? extends T> items) throws Exception {
		items.forEach(item -> log.info(String.valueOf(item)));
	}
}