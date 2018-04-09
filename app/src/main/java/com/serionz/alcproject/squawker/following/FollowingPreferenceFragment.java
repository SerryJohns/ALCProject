package com.serionz.alcproject.squawker.following;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.serionz.alcproject.R;

/**
 * Created by johnpaulseremba on 06/04/2018.
 */

public class FollowingPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->preferences_squawker
        addPreferencesFromResource(R.xml.following_sqauwker);
    }
}
