package org.libnova;

/**
 * Represent object declination.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
public class Dec {
	/**
	 * Construct RA from double number.
	 */
	public Dec (double dec)
	{
		this.dec = dec;
	};

	/**
	 * Returns string representation of the declination.
	 */
	public String toString()
	{
		return new HMS(dec).toStringWithSign("00.00");
	}
	
	private double dec;
}
