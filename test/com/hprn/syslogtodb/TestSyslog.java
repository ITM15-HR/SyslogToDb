package com.hprn.syslogtodb;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.hprn.syslogtodb.handler.SyslogFileHandler;
import com.hprn.syslogtodb.helper.ExportType;
import com.hprn.syslogtodb.helper.TimeInterval;
import com.hprn.syslogtodb.model.SyslogData;
import com.hprn.syslogtodb.model.SyslogHeader;
import com.hprn.syslogtodb.model.SyslogMessageIF;
import com.hprn.syslogtodb.model.SyslogPriority;

public class TestSyslog {

	//private static final String LOG_FILENAME = "messages_2016_11_13";
	private static final String LOG_FILENAME = "messages_example_zywall.txt";

	@Test
	public void testPriority() {
		SyslogPriority sp = new SyslogPriority(143);
		assertEquals(17, sp.getFacility());
		assertEquals(7, sp.getSeverty());
	}
	
    @Test
    public void testZyWallSyslog() {
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	assertEquals(10, syslog.size());
    }
    
    @Test
    public void testZyWallSyslogFilter() {
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	ZyWallSyslog dropped = new ZyWallSyslog();
		dropped.setData(syslog.findDataInMessage("DROP"));
		assertEquals(2, dropped.size());
    }
    
    @Test
    public void testZyWallSyslogStatisticPerSecond() {
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	assertEquals(8, syslog.countPer(TimeInterval.SECOND));
    }
    
    @Test
    public void testZyWallSyslogStatisticPerMinute() {
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	assertEquals(5, syslog.countPer(TimeInterval.MINUTE));
    }
    
    @Test
    public void testZyWallSyslogStatisticPerHour() {
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	assertEquals(4, syslog.countPer(TimeInterval.HOUR));
    }
    
    @Test
    public void testZyWallSyslogStatisticPerDay() {
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	assertEquals(3, syslog.countPer(TimeInterval.DAY));
    }
    
    @Test
    public void testZyWallSyslogStatisticPerDayAsCSV() {
    	String result = "2016-10-11T00:00:00+02:00;2\n"+
    			"2016-10-12T00:00:00+02:00;5\n"+
    			"2016-10-10T00:00:00+02:00;3";
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	syslog.countPer(TimeInterval.DAY);
    	String str = syslog.exportStatistic(ExportType.CSV);
    	assertEquals(result, str);
    }
    
    @Test
    public void testZyWallSyslogStatisticPerDayAsXML() {
    	String result = "<statistics>\n"+
				"	<statistic>\n"+
				"		<date>2016-10-11T00:00:00+02:00</date>\n"+
				"		<value>2</value>\n"+
				"	</statistic>\n"+
				"	<statistic>\n"+
				"		<date>2016-10-12T00:00:00+02:00</date>\n"+
				"		<value>5</value>\n"+
				"	</statistic>\n"+
				"	<statistic>\n"+
				"		<date>2016-10-10T00:00:00+02:00</date>\n"+
				"		<value>3</value>\n"+
				"	</statistic>\n"+
				"</statistics>";
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	syslog.countPer(TimeInterval.DAY);
    	String str = syslog.exportStatistic(ExportType.XML);
    	assertEquals(result, str);
    }
    
    @Test
    public void testZyWallSyslogStatisticPerDayAsJSON() {
    	String result = "{\"statistics\":[\n"+
				"\t{\"date\":\"2016-10-11T00:00:00+02:00\",\"value\":2},\n"+
				"\t{\"date\":\"2016-10-12T00:00:00+02:00\",\"value\":5},\n"+
				"\t{\"date\":\"2016-10-10T00:00:00+02:00\",\"value\":3}\n"+
				"]}";
    	ZyWallSyslog syslog = new ZyWallSyslog();
    	syslog.read(LOG_FILENAME);
    	syslog.countPer(TimeInterval.DAY);
    	String str = syslog.exportStatistic(ExportType.JSON);
    	assertEquals(result, str);
    }
	
}
