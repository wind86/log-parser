package com.parser.factory;

import java.util.Date;

public abstract class AbstractDurationPeriodFactory implements DurationPeriodFactory {

	protected Date initialDate;

	public AbstractDurationPeriodFactory(Date date) {
		this.initialDate = date;
	}

	@Override
	public Date createFromDate() {
		return initialDate;
	}
}