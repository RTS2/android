/**
 * RTS2 Android application settings.
 *
 * @author Petr Kubanek <petr@kubanek.net>
 */
package org.rts2.android;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);        
        addPreferencesFromResource(R.layout.settings);        
    }
}
