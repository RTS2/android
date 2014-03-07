package org.rts2.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

import org.rts2.JSON;
import org.libnova.RADec;

class RetreiveRaDecTask extends AsyncTask<TextView, Void, RADec> {
    private Exception exception;

    private TextView allViews[];

    protected RADec doInBackground(TextView... views) {
        try {
            JSON json = new JSON ("http://b3.rts2.org/andor", "petr", "test");
	    allViews = views;
	    return json.getValueRADec("T0", "TEL");
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

public class TelescopeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final String HOST_NAME = "b3.rts2.org";
        
        TextView ra = (TextView)findViewById(R.id.RA);
        TextView dec = (TextView)findViewById(R.id.DEC);

	new RetreiveRaDecTask().execute(ra, dec);
    }
}
