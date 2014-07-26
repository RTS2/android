package org.libnova;

import org.libnova.HMS;

/**
 * Represent object Azimuth.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
public class Azimuth {
	/**
	 * Construct Azimuth from double number.
	 */
	public Azimuth (double az)
	{
		this.az = az;
	};

	/**
	 * Returns string representation of the Azimuth.
	 */
	public String toString()
	{
		return new HMS(az).toString();
	}
	
	private double az;
}
