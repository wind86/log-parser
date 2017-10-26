package com.parser.entity;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@FieldDefaults(level = PRIVATE)
public class BannedIpEntity {
	Long id;
	Long jobId;
	String ip;
	String reason;
}