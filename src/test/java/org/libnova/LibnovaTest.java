/*
 * Libnova Tests. Copyright Petr Kubanek <petr@kubanek.net>
 */
package org.libnova;

import org.libnova.RA;
import org.libnova.Dec;
import org.libnova.RADec;
import org.libnova.HMS;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LibnovaTest extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public LibnovaTest(String testName)
	{
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite(LibnovaTest.class);
	}

	/**
	 * Testing RA
	 */
	public void testRA1() throws Exception
	{
		RA ra = new RA(15.0);
		assertEquals("RA 15.0 == 01:00:00.000 in HMS", "01:00:00.000", ra.toString());
	}

	public void testRA2() throws Exception
	{
		RA ra = new RA(21.1234678);
		assertEquals("RA 21.1234678 == 01:24:29.632 in HMS", "01:24:29.632", ra.toString());
	}

	/**
	 * Testing DEC
	 */
	public void testDec1() throws Exception
	{
		Dec dec = new Dec(15.0);
		assertEquals("Declination 15.0 == +15:00:00.00 in DMS", "+15:00:00.00", dec.toString());
	}

	public void testDec2() throws Exception
	{
		Dec dec = new Dec(-15.0);
		assertEquals("Declination -15.0 == -15:00:00.00 in DMS", "-15:00:00.00", dec.toString());
	}

	public void testDec3() throws Exception
	{
		Dec dec = new Dec(-87.999999);
		assertEquals("Declination -87.999999 == -88:00:00.00 in DMS", "-88:00:00.00", dec.toString());
	}

	/**
	 * Test RA Dec
	 */
	public void testRADec1() throws Exception
	{
		RADec raDec = new RADec(21.1234678, -87.999999);
		assertEquals("RADec 21.1234678 -87.999999 == 01:24:29.632 -88:00:00.00", "01:24:29.632 -88:00:00.00", raDec.toString());
	}
}
