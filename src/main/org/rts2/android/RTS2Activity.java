package org.rts2.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

import org.rts2.JSON;

class RetreiveRaDecTask extends AsyncTask<TextView, Void, String> {
    private Exception exception;

    private TextView allViews[];

    protected String doInBackground(TextView... views) {
        try {
            JSON json = new JSON ("http://b3.rts2.org/andor", "petr", "test");
	    allViews = views;
	    return json.getValue("T0", "infotime");
        } catch (Exception e) {
            Log.e("doInBackground", "error while retrieving", e);
	    this.exception = e;
        }
	return null;
    }

    protected void onPostExecute(String val) {
        Log.e("onPostExecute", "running");
        try
	{
       	    allViews[0].setText("RA " + val);
	} catch (Exception e) {
	    Log.e("onPostExecute", "error ", e);
	}

    }
}

public class RTS2Activity extends Activity {
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
