package com.parser.factory;

import static java.util.Calendar.HOUR_OF_DAY;
import static org.apache.commons.lang3.time.DateUtils.addHours;
import static org.apache.commons.lang3.time.DateUtils.truncate;

import java.util.Date;

public class HourlyDurationFactory extends AbstractDurationPeriodFactory {

	public HourlyDurationFactory(Date date) {
		super(truncate(date, HOUR_OF_DAY));
	}

	@Override
	public Date createTillDate() {
		return addHours(initialDate, 1);
	}
}