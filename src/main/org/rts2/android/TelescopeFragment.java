package org.rts2.android;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.rts2.JSON;
import org.libnova.RADec;

/**
 * Callback class for retrieving of JSON data.
 *
 * @author Petr Kubánek <petr@kubanek.net>
 */
class RetreiveRaDecTask extends AsyncTask<TextView, Void, RADec> {
    private Exception exception;

    private TextView allViews[];

    private String url;
    private String username;
    private String password;

    public RetreiveRaDecTask(String url, String username, String password) {
	this.url = url;
	this.username = username;
	this.password = password;
    }

    protected RADec doInBackground(TextView... views) {
        try {
            JSON json = new JSON (url, username, password);
	    allViews = views;
	    String[] telescopes = json.devbytype(JSON.DEVICE_TYPE_MOUNT);
	    if (telescopes.length > 0)
	    {
	    	return json.getValueRADec(telescopes[0], "TEL");
	    }
        } catch (Exception e) {
            Log.e("doInBackground", "error while retrieving", e);
	    this.exception = e;
        }
	return null;
    }

    protected void onPostExecute(RADec val) {
        try
	{
       	    allViews[0].setText("RA " + val.getRA().toString());
       	    allViews[1].setText("Dec " + val.getDec().toString());
	} catch (Exception e) {
	    Log.e("onPostExecute", "error ", e);
	}

    }
}


/**
 * Telescope activity display.
 *
 * @author Petr Kubánek <petr@kubanek.net>
 */
public class TelescopeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int ITEM_SETTINGS = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    /** Called when the view is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
	View view = inflater.inflate(R.layout.telescope, container, false);

	return view;
    }

    @Override
    public void onStart() {
    	super.onStart();

	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
	sharedPrefs.registerOnSharedPreferenceChangeListener(this);

    	refresh(sharedPrefs);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
        refresh(sharedPrefs);
    }

    private void refresh(SharedPreferences sharedPrefs) {
        TextView ra = (TextView)getView().findViewById(R.id.RA);
        TextView dec = (TextView)getView().findViewById(R.id.DEC);

	new RetreiveRaDecTask(sharedPrefs.getString("url", "null"), sharedPrefs.getString("username", "null"), sharedPrefs.getString("password", "null")).execute(ra, dec);
    }
}
