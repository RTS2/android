package org.rts2.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import org.rts2.JSON;
import org.libnova.RADec;

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
	    	return json.getValueRADec(telescopes[0], "TEL");
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

public class TelescopeActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int ITEM_SETTINGS = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	sharedPrefs.registerOnSharedPreferenceChangeListener(this);

	refresh(sharedPrefs);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
        refresh(sharedPrefs);
    }

    private void refresh(SharedPreferences sharedPrefs) {
        TextView ra = (TextView)findViewById(R.id.RA);
        TextView dec = (TextView)findViewById(R.id.DEC);

	new RetreiveRaDecTask(sharedPrefs.getString("url", "null"), sharedPrefs.getString("username", "null"), sharedPrefs.getString("password", "null")).execute(ra, dec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add(Menu.NONE, ITEM_SETTINGS, 0, getString(R.string.settings));
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	Log.i("onOptionsItemSelected", "selected item");
	switch (item.getItemId())
	{
	   case ITEM_SETTINGS:
	      startActivity(new Intent(this, SettingsActivity.class));
	      return true;
	}
	return false;
    }
}
