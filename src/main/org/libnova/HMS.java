/**
 *
 */
package org.libnova;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Utility functions for HMS object.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
public class HMS {
	/**
	 * Construct HMS from double number.
	 */
	public HMS (double hms)
	{
		sign = hms >= 0;

		hms = Math.abs(hms);

		h = (int) hms;
		hms -= h;
		hms *= 60.0;
		m = (int) hms;
		hms -= m;
		s = hms * 60.0;
	};

	/**
	 * Returns string representation of the RA.
	 * RA is assumed to be in degrees, e.g. in range 0-360.
	 */
	public String toString()
	{
		return toString("00.000");
	}

	public String toString(String secondsDecimalFormat)
	{
		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');

		DecimalFormat format = new DecimalFormat(secondsDecimalFormat, custom);

		// check if string is rounded above 60.00
		String sformat = format.format(s);
		if (Double.parseDouble(sformat) >= 60.0)
		{
			s -= 60.0;
			s = Math.abs(s);
			m += 1;
			if (m == 60)
			{
				m = 0;
				h += 1;
			}

			sformat = format.format(s);
		}

		return String.format("%02d:%02d:%s", h, m, sformat);
	}

	/**
	 * Returns RA/DEC string representation of HMS with sign.
	 */
	public String toStringWithSign()
	{
		return String.format("%c%s", sign ? '+' : '-', toString());
	}

	/**
	 * Returns RA/DEC string representation of HMS with sign and selected separator.
	 */
	public String toStringWithSign(String secondsDecimalFormat)
	{
		return String.format("%c%s", sign ? '+' : '-', toString(secondsDecimalFormat));
	}
	
	private int h;
	private int m;
	private double s;
	// false for negative numbers, true for positive
	private boolean sign;
}
