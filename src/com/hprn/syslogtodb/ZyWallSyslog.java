package com.hprn.syslogtodb;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hprn.syslogtodb.handler.SyslogFileHandler;
import com.hprn.syslogtodb.helper.ExportType;
import com.hprn.syslogtodb.helper.TimeInterval;
import com.hprn.syslogtodb.model.SyslogData;
import com.hprn.syslogtodb.model.SyslogHeader;
import com.hprn.syslogtodb.model.SyslogMessageIF;
import com.hprn.syslogtodb.model.SyslogPriority;

public class ZyWallSyslog {
	
	private List<SyslogData> data = new ArrayList<SyslogData>(); 
	private Map<Calendar,Integer> statistic = new HashMap<Calendar,Integer>();
	private Map<String,Integer> uniqueSource = new HashMap<String, Integer>();
	private Map<String,Integer> uniqueDestination = new HashMap<String, Integer>();

	public void read(String filename) {
		SyslogFileHandler file = new SyslogFileHandler(filename);
		try {
			List<String> data = file.read();
			for (String dataString : data) {
				String[] logFileData = dataString.split(" ");
				SyslogData syslogData = new SyslogData();
				SyslogHeader syslogHeader = new SyslogHeader();
				//SyslogStructuredData syslogStructuredData = new SyslogStructuredData();
				SyslogMessageIF syslogMessage;
				
				int counter = 0;
				int beginIndex = 0;
				String msg =  "";
				StringBuilder sb = new StringBuilder();
				for (String string : logFileData) {
					counter++;
					if (counter > 7) {
						beginIndex += counter;
						msg = dataString.substring(beginIndex-1, dataString.length());
						break;
					} else {
						beginIndex += string.length();
						switch(counter) {
							case 1:
								int start, end;
								start = string.indexOf("<");
								end = string.indexOf(">");
								SyslogPriority prio = new SyslogPriority(Integer.parseInt(string.substring(start+1, end)));
								syslogHeader.setPriority(prio);
								syslogHeader.setVersion(Integer.parseInt(string.substring(end+1, string.length())));
								break;
							case 2:
								Calendar cal = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
								cal.setTime(sdf.parse(string));
								syslogHeader.setDateTime(cal);
								break;
							case 3:
								syslogHeader.setHost(string);
								break;
							case 4:
								syslogHeader.setAppName(string);
								break;
							case 5:
								syslogHeader.setProcid(string);
								break;
							case 6: 
								syslogHeader.setMsgid(string);
								break;
							case 7:
								//StructuredData
								break;
						}
					}
				}
				syslogMessage = new SyslogMessageZyWall(msg);
				syslogData.setHeader(syslogHeader);
				syslogData.setMessage(syslogMessage);
				if (((SyslogMessageZyWall) syslogMessage).isExpectedToBeZyWallMsg())
					add(syslogData);
			}
		} catch (FileNotFoundException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
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
				return statistic.size();
			case MINUTE:
				countByMinute();
				return statistic.size();
			case HOUR:
				countByHour();
				return statistic.size();
			case DAY:
				countByDay();
				return statistic.size();
		}
		return -1;
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
	        sb.append(sdf.format(cal.getTime())).append(";").append(pair.getValue());
	        if (it.hasNext())
	        	sb.append("\n");
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
	        sb.append("\t{");
	        sb.append("\"date\":").append("\"").append(sdf.format(cal.getTime())).append("\",");
	        sb.append("\"value\":").append(pair.getValue()).append("}");
	        if (it.hasNext())
	        	sb.append(",");
	        sb.append("\n");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    sb.append("]").append("}");
		return sb.toString();
	}
	
	public String exportSources(ExportType exportType) {
		switch (exportType) {
			case CSV: 
				return exportSourcesToCSV();
			case XML:
				return exportSourcesToXML();
			case JSON:
				return exportSourcesToJSON();
			default:
				return null;
		}
	}
	
	private String exportSourcesToCSV() {
		// TODO Auto-generated method stub
		return null;
	}

	private String exportSourcesToXML() {
		// TODO Auto-generated method stub
		return null;
	}

	private String exportSourcesToJSON() {
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("rawtypes")
		Iterator it = uniqueSource.entrySet().iterator();
		sb.append("{\"").append("sources").append("\":[\n");
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        String src = (String) pair.getKey();
	        sb.append("\t{");
	        sb.append("\"source\":").append("\"").append(src).append("\",");
	        sb.append("\"value\":").append(pair.getValue()).append("}");
	        if (it.hasNext())
	        	sb.append(",");
	        sb.append("\n");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    sb.append("]").append("}");
		return sb.toString();
	}

	public void countBySource(boolean includePorts, int minCount) {
		uniqueSource.clear();
		String terminatingString;
		if (includePorts)
			terminatingString = "\" ";
		else
			terminatingString = ":";
		for (SyslogData syslogData : data) {
			int idxSrcStart = syslogData.getMessage().message().indexOf("src=\"") + 5;
			String src = syslogData.getMessage().message().substring(idxSrcStart);
			src = src.substring(0, src.indexOf(terminatingString));
			if (!uniqueSource.containsKey(src)) {
				uniqueSource.put(src, 1);
			} else {
				uniqueSource.put(src, uniqueSource.get(src) + 1);
			}
		}
		
		if (minCount > 0) {
			Map<String, Integer> filteredSource = new HashMap<String, Integer>();
			Iterator it = uniqueSource.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				String src = (String) pair.getKey();
				int value = (Integer)pair.getValue();
		        if (value >= minCount)
		        	filteredSource.put(src, value);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
			uniqueSource = filteredSource;
		}
	}

	
	
	
}
