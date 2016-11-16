package com.hprn.syslogtodb;

import com.hprn.syslogtodb.model.SyslogMessageIF;

public class SyslogMessageZyWall implements SyslogMessageIF {
	
	private String msg;
	private String[] splittedMsg;
	private String separator = "\" ";
	
	public SyslogMessageZyWall(String msg) {
		setMessage(msg);
	}

	@Override
	public void setMessageSeparator(String separator) {
		this.separator = separator;
	}

	@Override
	public void setMessage(String msg) {
		this.msg = msg;
		splittedMsg = msg.substring(msg.indexOf(" "), msg.length()).split(separator);
	}

	@Override
	public String message() {
		return msg;
	}

	@Override
	public String[] messages() {
		return splittedMsg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("separator: ").append("\n");
		sb.append("message: ").append(msg);
		return sb.toString();
	}
	
}
