package org.helit.sonoclapper;

import android.content.res.Resources;
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
        Resources res = getResources();
        getPreferenceScreen().findPreference("audio_version").setSummary(String.format(Locale.getDefault(), "Version %d", ToneGenerator.getVersion()));
        getPreferenceScreen().findPreference("internal_version").setSummary(String.format(Locale.getDefault(), "Revision %d", res.getInteger(R.integer.internal_revision)));
    }
}