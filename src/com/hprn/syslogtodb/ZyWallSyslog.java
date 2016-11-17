package com.hprn.syslogtodb;

import java.util.ArrayList;
import java.util.List;

import com.hprn.syslogtodb.helper.TimeInterval;
import com.hprn.syslogtodb.model.SyslogData;

public class ZyWallSyslog {
	
	private List<SyslogData> data = new ArrayList<SyslogData>(); 

	public void add(SyslogData syslogData) {
		if (syslogData == null)
			throw new IllegalArgumentException("A null parameter is not allowed");
		data.add(syslogData);
	}
	
	public void addData(List<SyslogData> data) {
		for (SyslogData syslogData : data) {
			this.data.add(syslogData);
		}
	}
	
	public void setData(List<SyslogData> data) {
		this.data.clear();
		for (SyslogData syslogData : data) {
			this.data.add(syslogData);
		}
	}
	
	public List<SyslogData> allData() {
		List<SyslogData> theData = new ArrayList<SyslogData>();
		for (SyslogData syslogData : data) {
			theData.add(syslogData);
		}
		return theData;
	}
	
	public List<SyslogData> findDataInMessage(String searchValue) {
		if (searchValue == null || searchValue.equals(""))
			throw new IllegalArgumentException("A null or empty search string is not allowed");
		List<SyslogData> theData = new ArrayList<SyslogData>();
		for (SyslogData syslogData : data) {
			if (syslogData.getMessage().message().indexOf(searchValue) > 0)
				theData.add(syslogData);
		}
		return theData;
	}

	public int size() {
		return data.size();
	}
	
	//ToDo: Think about how to analyze data. Maybe a HashMap is needed. Key is SECOND, MINUTE, DAY, HOUR and value is count 
	public int countPer(TimeInterval timeInterval) {
		switch (timeInterval) {
			case SECOND:
				return countBySecond();
			case MINUTE:
				return countByMinute();
			case HOUR:
				return countByHour();
			case DAY:
				return countByDay();
		}
		return 0;
	}

	private int countBySecond() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int countByMinute() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int countByHour() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int countByDay() {
		// TODO Auto-generated method stub
		return 0;
	}

}
