package org.rts2.android;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

/**
 * Telescope activity display.
 *
 * @author Petr Kubánek <petr@kubanek.net>
 */
public class ObservatoryActivity extends Activity {
    private static final int ITEM_SETTINGS = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onStart() {
    	super.onStart();
	FragmentManager fragmentManager = getFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	TelescopeFragment telescope = new TelescopeFragment();
	//fragmentTransaction.add(R.layout.main,telescope);
	fragmentTransaction.commit();
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
	switch (item.getItemId())
	{
	   case ITEM_SETTINGS:
	      startActivity(new Intent(this, SettingsActivity.class));
	      return true;
	}
	return false;
    }
}
