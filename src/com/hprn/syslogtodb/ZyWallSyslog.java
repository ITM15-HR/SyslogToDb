package com.hprn.syslogtodb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hprn.syslogtodb.helper.ExportType;
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
		for (SyslogData syslogData : data) {
			int day, month, year, hour, minute, second;
			day = syslogData.getHeader().getDateTime().get(Calendar.DATE);
			month = syslogData.getHeader().getDateTime().get(Calendar.MONTH);
			year = syslogData.getHeader().getDateTime().get(Calendar.YEAR);
			hour = syslogData.getHeader().getDateTime().get(Calendar.HOUR_OF_DAY);
			minute = syslogData.getHeader().getDateTime().get(Calendar.MINUTE);
			second = syslogData.getHeader().getDateTime().get(Calendar.SECOND);
			Calendar theDay = Calendar.getInstance();
			theDay.set(year,  month, day, hour, minute, second);
			theDay.set(Calendar.MILLISECOND, 0);
			theDay.setTimeZone(syslogData.getHeader().getDateTime().getTimeZone());
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
		for (SyslogData syslogData : data) {
			int day, month, year, hour, minute;
			day = syslogData.getHeader().getDateTime().get(Calendar.DATE);
			month = syslogData.getHeader().getDateTime().get(Calendar.MONTH);
			year = syslogData.getHeader().getDateTime().get(Calendar.YEAR);
			hour = syslogData.getHeader().getDateTime().get(Calendar.HOUR_OF_DAY);
			minute = syslogData.getHeader().getDateTime().get(Calendar.MINUTE);
			Calendar theDay = Calendar.getInstance();
			theDay.set(year,  month, day, hour, minute, 0);
			theDay.set(Calendar.MILLISECOND, 0);
			theDay.setTimeZone(syslogData.getHeader().getDateTime().getTimeZone());
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
		for (SyslogData syslogData : data) {
			int day, month, year, hour;
			day = syslogData.getHeader().getDateTime().get(Calendar.DATE);
			month = syslogData.getHeader().getDateTime().get(Calendar.MONTH);
			year = syslogData.getHeader().getDateTime().get(Calendar.YEAR);
			hour = syslogData.getHeader().getDateTime().get(Calendar.HOUR_OF_DAY);
			Calendar theDay = Calendar.getInstance();
			theDay.set(year,  month, day, hour, 0, 0);
			theDay.set(Calendar.MILLISECOND, 0);
			theDay.setTimeZone(syslogData.getHeader().getDateTime().getTimeZone());
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
		for (SyslogData syslogData : data) {
			int day, month, year;
			day = syslogData.getHeader().getDateTime().get(Calendar.DATE);
			month = syslogData.getHeader().getDateTime().get(Calendar.MONTH);
			year = syslogData.getHeader().getDateTime().get(Calendar.YEAR);
			Calendar theDay = Calendar.getInstance();
			theDay.set(year,  month, day, 0, 0, 0);
			theDay.set(Calendar.MILLISECOND, 0);
			theDay.setTimeZone(syslogData.getHeader().getDateTime().getTimeZone());
			if (!statistic.containsKey(theDay)) {
				statistic.put(theDay, 1);
			} else {
				statistic.put(theDay, statistic.get(theDay) + 1);
			}
		}
		System.out.println(String.format("Day: %d", statistic.size()));
	}
	
	public String exportStatistic(ExportType exportType) {
		switch (exportType) {
			case CSV: 
				return exportStatisticToCSV();
			case XML:
				return exportStatisticToXML();
			case JSON:
				return exportStatisticToJSON();
			default:
				return null;
		}
	}

	private String exportStatisticToCSV() {
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("rawtypes")
		Iterator it = statistic.entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	        Calendar cal = Calendar.getInstance();
	        cal = (Calendar) pair.getKey();
	        sb.append(sdf.format(cal.getTime())).append(";").append(pair.getValue()).append("\n");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return sb.toString();
	}
	
	private String exportStatisticToXML() {
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("rawtypes")
		Iterator it = statistic.entrySet().iterator();
		sb.append("<statistics>").append("\n");
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	        Calendar cal = Calendar.getInstance();
	        cal = (Calendar) pair.getKey();
	        sb.append("\t<statistic>\n");
	        sb.append("\t\t<date>").append(sdf.format(cal.getTime())).append("</date>").append("\n");
	        sb.append("\t\t<value>").append(pair.getValue()).append("</value>").append("\n");
	        sb.append("\t</statistic>\n");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    sb.append("</statistics>");
		return sb.toString();
	}
	
	private String exportStatisticToJSON() {
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("rawtypes")
		Iterator it = statistic.entrySet().iterator();
		sb.append("{\"").append("statistics").append("\":[\n");
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	        Calendar cal = Calendar.getInstance();
	        cal = (Calendar) pair.getKey();
	        sb.append("\t{\"").append(sdf.format(cal.getTime())).append("\":").append(pair.getValue()).append("}");
	        if (it.hasNext())
	        	sb.append(",");
	        sb.append("\n");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    sb.append("]").append("}");
		return sb.toString();
	}

}
