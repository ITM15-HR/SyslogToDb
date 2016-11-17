package com.hprn.syslogtodb;

import com.hprn.syslogtodb.model.SyslogMessageIF;

public class SyslogMessageZyWall implements SyslogMessageIF {
	
	private String msg;
	private String[] splittedMsg;
	private String separator = "\" ";
	private boolean expectedToBeZyWallMsg = true;
	
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
		setMessages(msg);
	}

	private void setMessages(String msg) {
		splittedMsg = msg.substring(msg.indexOf(" ") + 1, msg.length()).split(separator);
		if (splittedMsg.length <= 1)
			expectedToBeZyWallMsg = false;
		for (int i = 0; i < splittedMsg.length; i++) {
			if (splittedMsg[i].lastIndexOf("\"") != splittedMsg[i].length()-1)
				if (splittedMsg[i].indexOf("\"") > 0)
					splittedMsg[i] += "\"";
		}
	}

	@Override
	public String message() {
		return msg;
	}

	@Override
	public String[] messages() {
		return splittedMsg;
	}
	
	public boolean isExpectedToBeZyWallMsg() {
		return expectedToBeZyWallMsg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("separator: ").append("\n");
		sb.append("message: ").append(msg);
		return sb.toString();
	}
	
}
