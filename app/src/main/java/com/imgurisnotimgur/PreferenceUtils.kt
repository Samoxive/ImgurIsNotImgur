package com.imgurisnotimgur

import android.content.SharedPreferences
import android.content.res.Resources

class PreferenceUtils {
    companion object {
        fun findIndexOfValue(values: Array<String>, value: String): Int {
            for (i in values.indices) {
                if (values[i].equals(value)) {
                    return i
                }
            }

            return 0
        }

        fun getGalleryParameters(preferences: SharedPreferences, resources: Resources): Triple<Int, Int, Boolean> {
            val sortPreferenceDefault = resources.getString(R.string.sort_default)
            val sectionPreferenceDefault = resources.getString(R.string.section_default)
            val sortPreference = preferences.getString("sort", sortPreferenceDefault)
            val sectionPreference = preferences.getString("section", sectionPreferenceDefault)

            val nsfwEnabled = preferences.getBoolean("nsfw_enabled", false)

            val sectionEntries = resources.getStringArray(R.array.section)
            val sortEntries = resources.getStringArray(R.array.sort)

            val sectionIndex = findIndexOfValue(sectionEntries, sectionPreference)
            val sortIndex = findIndexOfValue(sortEntries, sortPreference)

            return Triple(sectionIndex, sortIndex, nsfwEnabled)
        }
    }
}