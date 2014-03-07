package org.libnova;

import org.libnova.RA;
import org.libnova.RADec;

/**
 * Represent object RA and declination.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
public class RADec {
	/**
	 * Construct RADEC from double number.
	 */
	public RADec (double ra, double dec)
	{
		this.ra = ra;
		this.dec = dec;
	};

	public RA getRA()
	{
		return new RA(ra);
	}

	public Dec getDec()
	{
		return new Dec(dec);
	}

	/**
	 * Returns string representation of the declination.
	 */
	public String toString()
	{
		return getRA().toString() + " " + getDec().toString();
	}

	private double ra;
	private double dec;
}
