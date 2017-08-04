package org.helit.sonoclapper;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Add the version number to the corresponding summary
        getPreferenceScreen().findPreference("audio_version").setSummary(String.format(Locale.getDefault(), "Version %d", ToneGenerator.getVersion()));
    }
}