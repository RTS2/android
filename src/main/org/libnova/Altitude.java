package org.libnova;

import org.libnova.HMS;

/**
 * Represent object altitude (elevation).
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
public class Altitude {
	/**
	 * Construct Altitude from double number.
	 */
	public Altitude (double alt)
	{
		this.alt = alt;
	};

	/**
	 * Returns string representation of the altitude.
	 */
	public String toString()
	{
		return new HMS(alt).toString();
	}
	
	private double alt;
}
