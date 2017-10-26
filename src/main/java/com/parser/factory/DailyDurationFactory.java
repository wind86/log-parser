package com.parser.factory;

import static java.util.Calendar.DAY_OF_MONTH;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.apache.commons.lang3.time.DateUtils.truncate;

import java.util.Date;

public class DailyDurationFactory extends AbstractDurationPeriodFactory {

	public DailyDurationFactory(Date date) {
		super(truncate(date, DAY_OF_MONTH));
	}

	@Override
	public Date createTillDate() {
		return addDays(initialDate, 1);
	}
}