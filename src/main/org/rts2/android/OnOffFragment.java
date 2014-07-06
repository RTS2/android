package org.rts2.android;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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

import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.rts2.JSON;
import org.libnova.RADec;

/**
 * Callback class for retrieving of JSON data.
 *
 * @author Petr Kubánek <petr@kubanek.net>
 */
class RetreiveOnOffState extends AsyncTask<RadioGroup, Void, Long> {
    private Exception exception;

    private RadioGroup buttons;

    private Context context;

    private String url;
    private String username;
    private String password;
    
    private ProgressDialog pd = null;

    public RetreiveOnOffState(Context context, String url, String username, String password) {
        this.context = context;
	this.url = url;
	this.username = username;
	this.password = password;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
	pd.setTitle("Processing..");
	pd.setMessage("Please wait.");
	pd.setCancelable(false);
	pd.setIndeterminate(true);
	pd.show();
    }

    @Override
    protected Long doInBackground(RadioGroup... buttons) {
        try {
            JSON json = new JSON (url, username, password);
	    this.buttons = buttons[0];
	    return json.getState("centrald");
        } catch (Exception e) {
            Log.e("doInBackground", "error while retrieving", e);
	    this.exception = e;
        }
	return null;
    }

    protected void onPostExecute(Long val) {
        try
	{
       	    if (((val & JSON.CENTRALD_SOFT_OFF) == JSON.CENTRALD_SOFT_OFF) || ((val & JSON.CENTRALD_HARD_OFF) == JSON.CENTRALD_HARD_OFF))
	       buttons.check(R.id.off);
       	    else if ((val & JSON.CENTRALD_STANDY) == JSON.CENTRALD_STANDY)
	       buttons.check(R.id.standby);
	    else
	       buttons.check(R.id.on);
	    if (pd!=null)
	        pd.dismiss();
	} catch (Exception e) {
	    if (pd!=null)
	        pd.dismiss();
	    Log.e("onPostExecute", "error ", e);
	}

    }
}


/**
 * Telescope activity display.
 *
 * @author Petr Kubánek <petr@kubanek.net>
 */
public class OnOffFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int ITEM_SETTINGS = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    /** Called when the view is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
	View view = inflater.inflate(R.layout.observatory, container, false);

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
	new RetreiveOnOffState(getActivity(), sharedPrefs.getString("url", "null"), sharedPrefs.getString("username", "null"), sharedPrefs.getString("password", "null")).execute((RadioGroup)getView().findViewById(R.id.observatory_group));
    }
}
