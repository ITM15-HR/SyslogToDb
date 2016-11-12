package com.hprn.syslogtodb;

import org.junit.Test;

public class TestSyslog {

	@Test
	public void testPriority() {
		SyslogPriority sp = new SyslogPriority(143);
		System.out.println("----------");
		sp = new SyslogPriority(141);
		System.out.println("----------");
		sp = new SyslogPriority(137);
		System.out.println("----------");
	}
	
}
