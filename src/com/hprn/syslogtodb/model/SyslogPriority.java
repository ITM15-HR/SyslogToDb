package com.hprn.syslogtodb.model;

public class SyslogPriority {
	
	private int facility;
	private int severty;
	
	public SyslogPriority(int priority) {
		setValues(priority);
	}

	private void setValues(int priority) {
		String prio = Integer.toBinaryString(priority);
		if (prio.length() != 8) 
			throw new IllegalArgumentException("Priority value invalid");
		facility = Integer.parseInt(prio.substring(0, 5), 2);
		severty = Integer.parseInt(prio.substring(5, 8), 2);
	}

	public int getFacility() {
		return facility;
	}

	public int getSeverty() {
		return severty;
	}

}
