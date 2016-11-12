package com.hprn.syslogtodb.model;

import java.util.Calendar;

public class SyslogHeader {
	
	private SyslogPriority priority;
	private int version;
	private Calendar dateTime;
	private String host;
	private String appName;
	private String procid;
	private String msgid;
	public SyslogPriority getPriority() {
		return priority;
	}
	public void setPriority(SyslogPriority priority) {
		this.priority = priority;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Calendar getDateTime() {
		return dateTime;
	}
	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getProcid() {
		return procid;
	}
	public void setProcid(String procid) {
		this.procid = procid;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	
	

}
