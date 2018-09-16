package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class  EarthquakePrefrenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference minPreference=findPreference(getString(R.string.settings_min_magnitude_key));
            bindPrefrenceToValue(minPreference);
        }

        private void bindPrefrenceToValue(Preference minPreference) {
            minPreference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(minPreference.getContext());
            onPreferenceChange(minPreference,preferences.getString(minPreference.getKey(),""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String valueString=value.toString();
            preference.setSummary(valueString);
            return true;
        }
    }
}
