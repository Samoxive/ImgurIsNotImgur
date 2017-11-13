package com.imgurisnotimgur

/**
 * Created by ozankaraali on 13.11.2017.
 */

import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager

class PreferencesFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_general)

        val locationPreference = findPreference("location")
        val unitPreference = findPreference("units")

        locationPreference.onPreferenceChangeListener = this
        unitPreference.onPreferenceChangeListener = this

        val loc_prefs = PreferenceManager
                .getDefaultSharedPreferences(this.activity.applicationContext)
        onPreferenceChange(locationPreference, loc_prefs.getString(locationPreference.key, ""))

        val unit_prefs = PreferenceManager
                .getDefaultSharedPreferences(this.activity.applicationContext)
        onPreferenceChange(unitPreference, unit_prefs.getString(unitPreference.key, ""))
    }

    override fun onPreferenceChange(preference: Preference, value: Any?): Boolean {

        val stringValue = value!!.toString()

        if (preference is ListPreference) {

            val prefIndex = preference.findIndexOfValue(stringValue)

            if (prefIndex >= 0) {
                preference.setSummary(preference.entries[prefIndex])
            }
        } else {
            preference.summary = stringValue
        }
        return true
    }
}