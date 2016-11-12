package com.hprn.syslogtodb.model;

public interface SyslogMessageIF {

	public void setMessageSeparator(String separator);
	public void setMessage(String msg);
	public String message();
	public String[] messages();
	
}
