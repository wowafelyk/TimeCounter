package com.example.fenix.timecounter;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by fenix on 21.06.2015.
 * File used for managing user settings.
 *
 *
 *  @version 1.0 21.06.2015
 *  autor Felyk Volodymyr
 *
 * */
@SuppressWarnings("deprecation from api_11 (HONEYCOMB)")
public class PreferencesActivity extends PreferenceActivity {
    Preference dialogPreference;
    @Override
    public void onCreate(Bundle savedInstanseState){
        super.onCreate(savedInstanseState);
        addPreferencesFromResource(R.xml.preferences);
        dialogPreference = (Preference)getPreferenceScreen().findPreference("@string/pr_textcolor");
        dialogPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });
    }

}
