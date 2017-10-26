package com.parser.factory;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.parser.factory.DailyDurationFactory;
import com.parser.factory.DurationPeriodFactory;

public class DailyDurationFactoryTest {
	
	private DurationPeriodFactory durationPeriodFactory;
	
	public DailyDurationFactoryTest() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(1990, 1, 1);
		calendar.set(HOUR_OF_DAY, 8);
		calendar.set(MINUTE, 30);
		
		this.durationPeriodFactory = new DailyDurationFactory(calendar.getTime());
	}

	@Test
	public void testFromDateCreation() {
		Date fromDate = durationPeriodFactory.createFromDate();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);
		
		assertEquals(0, calendar.get(MINUTE));
		assertEquals(0, calendar.get(HOUR_OF_DAY));
		assertEquals(1, calendar.get(DAY_OF_MONTH));
		assertEquals(1, calendar.get(MONTH));
		assertEquals(1990, calendar.get(YEAR));
	}
	
	@Test
	public void testTillDateCreation() {
		Date tillDate = durationPeriodFactory.createTillDate();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tillDate);
		
		assertEquals(0, calendar.get(MINUTE));
		assertEquals(0, calendar.get(HOUR_OF_DAY));
		assertEquals(2, calendar.get(DAY_OF_MONTH));
		assertEquals(1, calendar.get(MONTH));
		assertEquals(1990, calendar.get(YEAR));
	}
}