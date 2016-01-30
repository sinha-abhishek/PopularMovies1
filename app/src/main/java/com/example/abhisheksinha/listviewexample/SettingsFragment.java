package com.example.abhisheksinha.listviewexample;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by abhisheksinha on 1/30/16.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_final);
    }
}