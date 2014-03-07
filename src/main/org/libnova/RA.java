package org.libnova;

import org.libnova.HMS;

/**
 * Represent object RA.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
public class RA {
	/**
	 * Construct RA from double number.
	 */
	public RA (double ra)
	{
		this.ra = ra;
	};

	/**
	 * Returns string representation of the RA.
	 * RA is assumed to be in degrees, e.g. in range 0-360.
	 */
	public String toString()
	{
		return new HMS(ra / 15.0).toString();
	}
	
	private double ra;
}
