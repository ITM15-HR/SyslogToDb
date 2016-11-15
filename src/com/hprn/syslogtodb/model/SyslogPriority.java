package com.hprn.syslogtodb.model;

import java.util.HashMap;

public class SyslogPriority {
	
	public static final String FACILITY_0 = "kernel messages";
	public static final String FACILITY_1 = "user-level messages";
	public static final String FACILITY_2 = "mail system";
	public static final String FACILITY_3 = "system daemons";
	public static final String FACILITY_4 = "security/authorization messages";
	public static final String FACILITY_5 = "messages generated internally by syslogd";
	public static final String FACILITY_6 = "line printer subsystem";
	public static final String FACILITY_7 = "network news subsystem";
	public static final String FACILITY_8 = "UUCP subsystem";
	public static final String FACILITY_9 = "clock daemon";
	public static final String FACILITY_10 = "security/authorization messages";
	public static final String FACILITY_11 = "FTP daemon";
	public static final String FACILITY_12 = "NTP subsystem";
	public static final String FACILITY_13 = "log audit";
	public static final String FACILITY_14 = "log alert";
	public static final String FACILITY_15 = "clock daemon (note 2)";
	public static final String FACILITY_16 = "local use 0 (local0)";
	public static final String FACILITY_17 = "local use 1 (local1)";
	public static final String FACILITY_18 = "local use 2 (local2)";
	public static final String FACILITY_19 = "local use 3 (local3)";
	public static final String FACILITY_20 = "local use 4 (local4)";
	public static final String FACILITY_21 = "local use 5 (local5)";
	public static final String FACILITY_22 = "local use 6 (local6)";
	public static final String FACILITY_23 = "local use 7 (local7)";
	
	public static final HashMap<Integer, String> FACILITIES = new HashMap<Integer, String>();
	static {
		FACILITIES.put(0, FACILITY_0);
		FACILITIES.put(1, FACILITY_1);
		FACILITIES.put(2, FACILITY_2);
		FACILITIES.put(3, FACILITY_3);
		FACILITIES.put(4, FACILITY_4);
		FACILITIES.put(5, FACILITY_5);
		FACILITIES.put(6, FACILITY_6);
		FACILITIES.put(7, FACILITY_7);
		FACILITIES.put(8, FACILITY_8);
		FACILITIES.put(9, FACILITY_9);
		FACILITIES.put(10, FACILITY_10);
		FACILITIES.put(11, FACILITY_11);
		FACILITIES.put(12, FACILITY_12);
		FACILITIES.put(13, FACILITY_13);
		FACILITIES.put(14, FACILITY_14);
		FACILITIES.put(15, FACILITY_15);
		FACILITIES.put(16, FACILITY_16);
		FACILITIES.put(17, FACILITY_17);
		FACILITIES.put(18, FACILITY_18);
		FACILITIES.put(19, FACILITY_19);
		FACILITIES.put(20, FACILITY_20);
		FACILITIES.put(21, FACILITY_21);
		FACILITIES.put(22, FACILITY_22);
		FACILITIES.put(23, FACILITY_23);
	}
	
	public static final String SERVERTY_0 = "Emergency: system is unusable";
	public static final String SERVERTY_1 = "Alert: action must be taken immediately";
	public static final String SERVERTY_2 = "Critical: critical conditions";
	public static final String SERVERTY_3 = "Error: error conditions";
	public static final String SERVERTY_4 = "Warning: warning conditions";
	public static final String SERVERTY_5 = "Notice: normal but significant condition";
	public static final String SERVERTY_6 = "Informational: informational messages";
	public static final String SERVERTY_7 = "Debug: debug-level messages";
	
	public static final HashMap<Integer, String> SEVERTIES = new HashMap<Integer, String>();
	static {
		SEVERTIES.put(0, SERVERTY_0);
		SEVERTIES.put(1, SERVERTY_1);
		SEVERTIES.put(2, SERVERTY_2);
		SEVERTIES.put(3, SERVERTY_3);
		SEVERTIES.put(4, SERVERTY_4);
		SEVERTIES.put(5, SERVERTY_5);
		SEVERTIES.put(6, SERVERTY_6);
		SEVERTIES.put(7, SERVERTY_7);
	}
	
	private int facility;
	private int severty;
	
	public SyslogPriority(int priority) {
		setValues(priority);
	}

	private void setValues(int priority) {
		facility = Math.abs(priority / 8);
		severty = priority - (8 * facility);
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
		sb.append("FACILITY: ").append(FACILITIES.get(facility)).append("\n");
		sb.append("SEVERTY: ").append(SEVERTIES.get(severty));
		return sb.toString();
	}

}
