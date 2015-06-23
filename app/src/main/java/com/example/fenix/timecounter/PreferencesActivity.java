package com.example.fenix.timecounter;

import android.os.Bundle;
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
    @Override
    public void onCreate(Bundle savedInstanseState){
        super.onCreate(savedInstanseState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
