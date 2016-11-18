package com.hprn.syslogtodb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hprn.syslogtodb.helper.TimeInterval;
import com.hprn.syslogtodb.model.SyslogData;

public class ZyWallSyslog {
	
	private List<SyslogData> data = new ArrayList<SyslogData>(); 
	private Map<Calendar,Integer> statistic = new HashMap<Calendar,Integer>();

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
				countBySecond();
				break;
			case MINUTE:
				countByMinute();
				break;
			case HOUR:
				countByHour();
				break;
			case DAY:
				countByDay();
				break;
		}
		return 0;
	}

	private void countBySecond() {
		statistic.clear();
		Calendar theDay = Calendar.getInstance();
		for (SyslogData syslogData : data) {
			int day, month, year, hour, minute, second;
			day = syslogData.getHeader().getDateTime().getTime().getDate();
			month = syslogData.getHeader().getDateTime().getTime().getMonth();
			year = syslogData.getHeader().getDateTime().getTime().getYear();
			hour = syslogData.getHeader().getDateTime().getTime().getHours();
			minute = syslogData.getHeader().getDateTime().getTime().getMinutes();
			second = syslogData.getHeader().getDateTime().getTime().getSeconds();
			theDay.set(year,  month, day, hour, minute, second);
			if (!statistic.containsKey(theDay)) {
				statistic.put(theDay, 1);
			} else {
				statistic.put(theDay, statistic.get(theDay) + 1);
			}
		}
		System.out.println(statistic.size());
	}

	private void countByMinute() {
		statistic.clear();
		Calendar theDay = Calendar.getInstance();
		for (SyslogData syslogData : data) {
			int day, month, year, hour, minute;
			day = syslogData.getHeader().getDateTime().getTime().getDate();
			month = syslogData.getHeader().getDateTime().getTime().getMonth();
			year = syslogData.getHeader().getDateTime().getTime().getYear();
			hour = syslogData.getHeader().getDateTime().getTime().getHours();
			minute = syslogData.getHeader().getDateTime().getTime().getMinutes();
			theDay.set(year,  month, day, hour, minute, 0);
			if (!statistic.containsKey(theDay)) {
				statistic.put(theDay, 1);
			} else {
				statistic.put(theDay, statistic.get(theDay) + 1);
			}
		}
		System.out.println(statistic.size());
	}

	private void countByHour() {
		statistic.clear();
		Calendar theDay = Calendar.getInstance();
		for (SyslogData syslogData : data) {
			int day, month, year, hour;
			day = syslogData.getHeader().getDateTime().getTime().getDate();
			month = syslogData.getHeader().getDateTime().getTime().getMonth();
			year = syslogData.getHeader().getDateTime().getTime().getYear();
			hour = syslogData.getHeader().getDateTime().getTime().getHours();
			theDay.set(year,  month, day, hour, 0, 0);
			if (!statistic.containsKey(theDay)) {
				statistic.put(theDay, 1);
			} else {
				statistic.put(theDay, statistic.get(theDay) + 1);
			}
		}
		System.out.println(statistic.size());
	}

	private void countByDay() {
		statistic.clear();
		Calendar theDay = Calendar.getInstance();
		for (SyslogData syslogData : data) {
			int day, month, year;
			day = syslogData.getHeader().getDateTime().getTime().getDate();
			month = syslogData.getHeader().getDateTime().getTime().getMonth();
			year = syslogData.getHeader().getDateTime().getTime().getYear();
			theDay.set(year,  month, day);
			if (!statistic.containsKey(theDay)) {
				statistic.put(theDay, 1);
			} else {
				statistic.put(theDay, statistic.get(theDay) + 1);
			}
		}
		System.out.println(statistic.size());
	}

}
