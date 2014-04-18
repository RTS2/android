/**
 * RTS2 Android application settings.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
package org.rts2.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.InputType;

public class SettingsActivity extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	setContentView(R.layout.settings);

	getFragmentManager().beginTransaction().replace(R.id.hostPrefs, new HostSettingsFragment()).commit();
    }

    private class HostSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {        
            super.onCreate(savedInstanceState);        
            addPreferencesFromResource(R.layout.host_settings);

	    for(int i=0;i<getPreferenceScreen().getPreferenceCount();i++)
	    {
	         initSummary(getPreferenceScreen().getPreference(i));
	    }
        }

	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
    
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference pref = findPreference(key);
     
            if (pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                pref.setSummary(listPref.getEntry());
            } else if (pref instanceof EditTextPreference) {
                if (((EditTextPreference) pref).getEditText().getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    EditTextPreference etPref = (EditTextPreference) pref;
                    String maskedPw = "";
                    if (etPref.getText() != null) {
                        for (int j = 0; j < etPref.getText().length(); j++) {
                            maskedPw = maskedPw + "*";
                        }
                        pref.setSummary(maskedPw);
                    }
                } else {
                    EditTextPreference etPref = (EditTextPreference) pref;
                    pref.setSummary(etPref.getText());
                }
            }
        }

        private void initSummary(Preference p) {
            if (p instanceof PreferenceCategory) {
                PreferenceCategory pCat = (PreferenceCategory)p;
                for(int i=0;i<pCat.getPreferenceCount();i++) {
                    initSummary(pCat.getPreference(i));
                }
            } else {
                updatePrefSummary(p);
            }
 
        }
 
        private void updatePrefSummary(Preference p) {
            if (p instanceof ListPreference) {
                ListPreference listPref = (ListPreference) p;
                p.setSummary(listPref.getEntry());
            }
            if (p instanceof EditTextPreference) {
                EditTextPreference editTextPref = (EditTextPreference) p;
		if (editTextPref.getEditText().getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    String maskedPw = "";
                    if (editTextPref.getText() != null) {
                        for (int j=0; j<editTextPref.getText().length(); j++)
                            maskedPw = maskedPw + "*";
                    }
                    p.setSummary(maskedPw);
                } else {
                    p.setSummary(editTextPref.getText());
                }
            }
	}
    }
}
