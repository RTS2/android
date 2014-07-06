package org.rts2;

import java.lang.String;
import java.lang.StringBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;

import org.json.JSONObject;
import org.json.JSONArray;

import org.libnova.RADec;

/**
 * Class to access RTS2 via JAVA objects. Provides method to extract data from
 * a single RTS2 server.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
public class JSON
{
	public static final int DEVICE_TYPE_UNKNOW     = 0;
	public static final int DEVICE_TYPE_SERVERD    = 1;
	public static final int DEVICE_TYPE_MOUNT      = 2;
	public static final int DEVICE_TYPE_CCD        = 3;
	public static final int DEVICE_TYPE_DOME       = 4;
	public static final int DEVICE_TYPE_WEATHER    = 5;
	public static final int DEVICE_TYPE_ROTATOR    = 6;
	public static final int DEVICE_TYPE_PHOT       = 7;
	public static final int DEVICE_TYPE_PLAN       = 8;
	public static final int DEVICE_TYPE_GRB        = 9;
	public static final int DEVICE_TYPE_FOCUS     = 10;
	public static final int DEVICE_TYPE_MIRROR    = 11;
	public static final int DEVICE_TYPE_CUPOLA    = 12;
	public static final int DEVICE_TYPE_FW        = 13;
	public static final int DEVICE_TYPE_AUGERSH   = 14;
	public static final int DEVICE_TYPE_SENSOR    = 15;
	
	public static final int DEVICE_TYPE_EXECUTOR  = 20;
	public static final int DEVICE_TYPE_IMGPROC   = 21;
	public static final int DEVICE_TYPE_SELECTOR  = 22;
	public static final int DEVICE_TYPE_XMLRPC    = 23;
	public static final int DEVICE_TYPE_INDI      = 24;
	public static final int DEVICE_TYPE_LOGD      = 25;
	public static final int DEVICE_TYPE_SCRIPTOR  = 26;
	public static final int DEVICE_TYPE_BB        = 27;

	// centrald states
	public static final int CENTRALD_STANDY       = 0x010;
	public static final int CENTRALD_SOFT_OFF     = 0x020;
	public static final int CENTRALD_HARD_OFF     = 0x030;

	/**
         * Construct connection to given URL.
         *
         * @param url Server URL
	 */
	public JSON(String url) throws Exception
	{
		this(url,null,null);
	}

	/**
         * Construct connection to given URL, using given username and password.
         *
         * @param url Server URL
         * @param login Server login
	 * @param password User password 
         */
        public JSON(String url, String login, String password) throws Exception
	{
		devicesArray = null;

		client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "java-rts2");

		URL parsedUrl = new URL(url);

		int port = parsedUrl.getPort();
		if (port == -1) {
			port = parsedUrl.getDefaultPort();
		}

		if (login != null && password != null)
		{
			client.getCredentialsProvider().setCredentials(new AuthScope(parsedUrl.getHost(), port), new UsernamePasswordCredentials(login, password));
		}

		connect (url);
	}

	/**
	 * Initiate connection to a server.
	 */
	public void connect(String url)
	{
		baseUrl = url;
	}

	/**
	 * Return JSON object with the given URL.
	 */
	public String getUrl(String url) throws Exception
	{
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 8);

		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}

		return sb.toString();
	}

	/**
	 * Return JSON object with device.
	 *
	 * @param device RTS2 device name
	 *
	 * @return JSONObject with device values (reply to get API call)
	 */
	public JSONObject get(String device) throws Exception
	{
		return new JSONObject(getUrl(Uri.parse(baseUrl).buildUpon().appendEncodedPath("api/get").appendQueryParameter("d",device).build().toString()));
	}

	/**
	 * Refresh devicesArray.
	 */
	private void refreshDevices() throws Exception
	{
		devicesArray = new JSONArray(getUrl(Uri.parse(baseUrl).buildUpon().appendEncodedPath("api/devices").appendQueryParameter("e","1").build().toString()));
	}

	/**
	 * Return name(s) of the connected devices.
	 */
	public String[] devices() throws Exception
	{
		if (devicesArray == null)
			refreshDevices();
		String[] ret = new String[devicesArray.length()];
		for (int i=0; i < devicesArray.length(); i++)
			ret[i] = devicesArray.getJSONArray(i).getString(0);
		return ret;
	}

	public String[] devbytype(int type) throws Exception
	{
		if (devicesArray == null)
			refreshDevices();
		List<String> ret = new ArrayList();
		for (int i=0; i < devicesArray.length(); i++)
		{
			if (devicesArray.getJSONArray(i).getInt(1) == type)
				ret.add(devicesArray.getJSONArray(i).getString(0));
		}
		return ret.toArray(new String[0]);
	}

	/**
	 * Return device state.
	 *
	 * @param device RTS2 device name
	 */
	public long getState(String device) throws Exception
	{
		return Long.parseLong(get(device).get("state").toString());
	}

	/**
	 * Return value from the given device.
	 *
	 * @param device RTS2 device name
	 * @param value value name
	 *
	 * @return value &lt;value&gt; from device &lt;device&gt;
	 */
	public String getValue(String device, String value) throws Exception
	{
		return get(device).getJSONObject("d").get(value).toString();
	}

	/**
	 * Returns double value from RTS2 device.
	 *
	 * @param device RTS2 device name
	 * @param value value name
	 *
	 * @return value &lt;value&gt; from device &lt;device&gt;
	 */
	public double getValueDouble(String device, String value) throws Exception
	{
		return Double.parseDouble(getValue(device, value));
	}

	/**
	 * Returns integer value from RTS2 device.
	 *
	 * @param device RTS2 device name
	 * @param value value name
	 *
	 * @return value &lt;value&gt; from device &lt;device&gt;
	 */
	public int getValueInteger(String device, String value) throws Exception
	{
		return Integer.parseInt(getValue(device, value));
	}

	/**
	 * Returns long value from RTS2 device.
	 *
	 * @param device RTS2 device name
	 * @param value value name
	 *
	 * @return value &lt;value&gt; from device &lt;device&gt;
	 */
	public long getValueLong(String device, String value) throws Exception
	{
		return Long.parseLong(getValue(device, value));
	}

	/**
	 * Returns date value from RTS2 device.
	 *
	 * @param device RTS2 device name
	 * @param value value name
	 *
	 * @return value &lt;value&gt; from device &lt;device&gt;
	 */
	public Date getValueDate(String device, String value) throws Exception
	{
		return new Date((long)getValueDouble(device, value));
	}

	/**
	 * Return RA DEC pair.
	 */
	public RADec getValueRADec(String device, String value) throws Exception
	{
		JSONObject json = get(device).getJSONObject("d").getJSONObject(value);
		return new RADec(Double.parseDouble(json.get("ra").toString()), Double.parseDouble(json.get("dec").toString()));
	}

	private DefaultHttpClient client;
	private String baseUrl;
	private JSONArray devicesArray;
}
