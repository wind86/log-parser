package com.parser.factory;

import java.util.Date;

public interface DurationPeriodFactory {
	Date createFromDate();
	Date createTillDate();
}