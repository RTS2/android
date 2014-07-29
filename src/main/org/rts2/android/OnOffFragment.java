package org.rts2.android;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.rts2.JSON;
import org.libnova.RADec;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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

class SetOnOffState extends AsyncTask<String, Void, Integer> {

	@Override
	protected Integer doInBackground(String... params) {
		String url = params[0];
		String username = params[1];
		String password = params[2];
		String command = params[3];

		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		HttpGet onOffRequest = new HttpGet(url + "/switchstate/" + command);
		onOffRequest.addHeader("Authorization", "Basic " +
				Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT));
		try {
			response = client.execute(onOffRequest);
			Log.i("Switch state response:", response.getStatusLine().toString());
			return response.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		sharedPrefs.registerOnSharedPreferenceChangeListener(this);

		RadioGroup onOffGroup = (RadioGroup) getView().findViewById(R.id.observatory_group);
		onOffGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String command = "off";
				final String url = sharedPrefs.getString("url", "null");
				final String username = sharedPrefs.getString("username", "null");
				final String password = sharedPrefs.getString("password", "null");
				switch (checkedId) {
					case R.id.on:
						command = "on";
						break;
					case R.id.off:
						command = "off";
						break;
					case R.id.standby:
						command = "standby";
						break;
				}

				if (url == "null" || username == "null" || password == "null") {
					return;
				}

				SetOnOffState task = new SetOnOffState();
				task.execute(url, username, password, command);

			}
		});

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
