package com.hprn.syslogtodb;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import com.hprn.syslogtodb.handler.SyslogFileHandler;
import com.hprn.syslogtodb.model.SyslogData;
import com.hprn.syslogtodb.model.SyslogHeader;
import com.hprn.syslogtodb.model.SyslogMessageIF;
import com.hprn.syslogtodb.model.SyslogPriority;

public class TestSyslog {

	private static final String LOG_FILENAME = "message_example_zywall.txt";

	@Test
	public void testPriority() {
		SyslogPriority sp = new SyslogPriority(143);
		assertEquals(17, sp.getFacility());
		assertEquals(7, sp.getSeverty());
	}
	
    @Test
	public void testReadLog() {
		SyslogFileHandler file = new SyslogFileHandler(LOG_FILENAME);
		try {
			List<String> data = file.read();
			for (String dataString : data) {
				String[] logFileData = dataString.split(" ");
				SyslogData syslogData = new SyslogData();
				SyslogHeader syslogHeader = new SyslogHeader();
				//SyslogStructuredData syslogStructuredData = new SyslogStructuredData();
				SyslogMessageIF syslogMessage;
				
				int counter = 0;
				StringBuilder sb = new StringBuilder();
				for (String string : logFileData) {
					counter++;
					if (counter > 7)
						//message
						if (counter <= logFileData.length)
							sb.append(string).append(" ");
						else 
							sb.append(string);
					else
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
								//dateTime
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
				syslogMessage = new SyslogMessageZyWall(sb.toString());
				syslogData.setHeader(syslogHeader);
				syslogData.setMessage(syslogMessage);
				System.out.println(syslogHeader.getPriority().toString());
				System.out.println(syslogMessage.toString());
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
