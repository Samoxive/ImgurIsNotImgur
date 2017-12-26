package com.imgurisnotimgur

import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.widget.Toast
import com.imgurisnotimgur.data.ImgurContract

class PreferencesFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.pref_general)

        val contentResolver = activity.contentResolver
        val globalPreferences = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)

        val sectionPreference = findPreference("section")
        val sortPreference = findPreference("sort")
        val subredditNamePreference = findPreference("subreddit")
        val clearCacheButton = findPreference("clear")
        clearCacheButton.setOnPreferenceClickListener {
            contentResolver.delete(ImgurContract.BASE_CONTENT_URI.buildUpon().appendPath("everything").build(), null, null)
            Toast.makeText(activity, getString(R.string.clear_cache_toast), Toast.LENGTH_LONG).show()
            true
        }

        sectionPreference.onPreferenceChangeListener = this
        sortPreference.onPreferenceChangeListener = this
        subredditNamePreference.onPreferenceChangeListener = this

        onPreferenceChange(sectionPreference, globalPreferences.getString(sectionPreference.key, ""))
        onPreferenceChange(sortPreference, globalPreferences.getString(sortPreference.key, ""))
        onPreferenceChange(subredditNamePreference, globalPreferences.getString(subredditNamePreference.key, ""))
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