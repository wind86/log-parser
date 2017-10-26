package com.parser.entity;

import static lombok.AccessLevel.PRIVATE;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = PRIVATE)
public class LogItemEntity {
	Long id;
	Long jobId;
	Date createdAt;
	String ip;
	String request;
	String status;
	String userAgent;
}