package com.mierzejewski.inzynierka;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

/**
 * Created by dom on 04/11/14.
 */
public class SettingsFragment extends PreferenceFragment
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        ListPreference currencyPref = (ListPreference) findPreference(getString(R.string.currency_key));
        currencyPref.setValueIndex(0);

        //PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
    }

}
