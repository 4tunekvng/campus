package com.example.campus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

public class MySettingsFragment extends PreferenceFragmentCompat {
    private Preference myPreference;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferencescreen, rootKey);
        myPreference= findPreference("Log Out");
        myPreference.setOnPreferenceClickListener(preference -> {

            // Log Out here
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
            Intent i = new Intent(getContext() , LoginActivity.class);
            myPreference.setIntent(i);
            return false;
        });

    }


}