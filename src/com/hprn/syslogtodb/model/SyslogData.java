package com.hprn.syslogtodb.model;

import java.util.Calendar;

public class SyslogData {
	
	private static final String NILVALUE = "-";

	private SyslogHeader header;
	private SyslogStructuredData structuredData;
	private SyslogMessageIF message;
	
	public SyslogData() {
		
	}

	public SyslogHeader getHeader() {
		return header;
	}

	public void setHeader(SyslogHeader header) {
		this.header = header;
	}

	public SyslogStructuredData getStructuredData() {
		return structuredData;
	}

	public void setStructuredData(SyslogStructuredData structuredData) {
		this.structuredData = structuredData;
	}

	public SyslogMessageIF getMessage() {
		return message;
	}

	public void setMessage(SyslogMessageIF message) {
		this.message = message;
	}
	
	
	
	
}
