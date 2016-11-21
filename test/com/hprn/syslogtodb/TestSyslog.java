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
	public void testReadLog() {
		SyslogFileHandler file = new SyslogFileHandler(LOG_FILENAME);
		ZyWallSyslog zyWallSyslog = new ZyWallSyslog();
		try {
			List<String> data = file.read();
			for (String dataString : data) {
				String[] logFileData = dataString.split(" ");
				SyslogData syslogData = new SyslogData();
				SyslogHeader syslogHeader = new SyslogHeader();
				//SyslogStructuredData syslogStructuredData = new SyslogStructuredData();
				SyslogMessageIF syslogMessage;
				
				int counter = 0;
				int beginIndex = 0;
				String msg =  "";
				StringBuilder sb = new StringBuilder();
				for (String string : logFileData) {
					counter++;
					if (counter > 7) {
						beginIndex += counter;
						msg = dataString.substring(beginIndex-1, dataString.length());
						break;
					} else {
						beginIndex += string.length();
						switch(counter) {
							case 1:
								int start, end;
								start = string.indexOf("<");
								end = string.indexOf(">");
								SyslogPriority prio = new SyslogPriority(Integer.parseInt(string.substring(start+1, end)));
								syslogHeader.setPriority(prio);
								syslogHeader.setVersion(Integer.parseInt(string.substring(end+1, string.length())));
								break;
							case 2:
								Calendar cal = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
								cal.setTime(sdf.parse(string));
								syslogHeader.setDateTime(cal);
								break;
							case 3:
								syslogHeader.setHost(string);
								break;
							case 4:
								syslogHeader.setAppName(string);
								break;
							case 5:
								syslogHeader.setProcid(string);
								break;
							case 6: 
								syslogHeader.setMsgid(string);
								break;
							case 7:
								//StructuredData
								break;
						}
					}
				}
				syslogMessage = new SyslogMessageZyWall(msg);
				syslogData.setHeader(syslogHeader);
				syslogData.setMessage(syslogMessage);
				if (((SyslogMessageZyWall) syslogMessage).isExpectedToBeZyWallMsg())
					zyWallSyslog.add(syslogData);
			}
			
			ZyWallSyslog dropped = new ZyWallSyslog();
			dropped.setData(zyWallSyslog.findDataInMessage("DROP"));
			for (SyslogData sD : dropped.allData()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
				System.out.println(sdf.format(sD.getHeader().getDateTime().getTime()) + ": " + sD.getMessage().toString());
			}
			zyWallSyslog.countPer(TimeInterval.SECOND);
			System.out.println(zyWallSyslog.exportStatistic(ExportType.CSV));
			
			zyWallSyslog.countPer(TimeInterval.MINUTE);
			System.out.println(zyWallSyslog.exportStatistic(ExportType.XML));
			
			zyWallSyslog.countPer(TimeInterval.HOUR);
			System.out.println(zyWallSyslog.exportStatistic(ExportType.CSV));
			
			zyWallSyslog.countPer(TimeInterval.DAY);
			System.out.println(zyWallSyslog.exportStatistic(ExportType.JSON));
			assertEquals(10, zyWallSyslog.size());
			
			
		} catch (FileNotFoundException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
