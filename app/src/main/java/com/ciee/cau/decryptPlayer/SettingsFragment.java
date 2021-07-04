package com.ciee.cau.decryptPlayer;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.ciee.cau.decryptPlayer.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    Preference mToolBarPreference;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey);

        mToolBarPreference = findPreference(getString(R.string.pref_key_toolbar));
        mToolBarPreference.setOnPreferenceClickListener(preference -> {
            getActivity().finish();
            return true;
        });
    }
}
