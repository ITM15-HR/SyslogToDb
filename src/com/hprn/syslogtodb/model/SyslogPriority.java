package com.hprn.syslogtodb.model;

public class SyslogPriority {
	
	private int facility;
	private int severty;
	
	public SyslogPriority(int priority) {
		setValues(priority);
	}

	private void setValues(int priority) {
		String prio = Integer.toBinaryString(priority);
		if (prio.length() > 8) 
			throw new IllegalArgumentException("Priority value invalid");
		prio = normalizeBinaryString(prio);
		facility = Integer.parseInt(prio.substring(0, 5), 2);
		severty = Integer.parseInt(prio.substring(5, prio.length()), 2);
	}

	private String normalizeBinaryString(String prio) {
		String binaryString = prio;
		if (binaryString.length() < 8) {
			for (int i = 0; i < 8 - binaryString.length(); i++) {
				binaryString = "0" + binaryString;
			}
		}
		return binaryString;
	}

	public int getFacility() {
		return facility;
	}

	public int getSeverty() {
		return severty;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("facility: ").append(facility).append("\n");
		sb.append("severty: ").append(severty);
		return sb.toString();
	}

}
