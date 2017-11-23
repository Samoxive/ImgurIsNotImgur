package com.imgurisnotimgur

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.*

class Settings {
    companion object {
        var prefs: OauthResult = OauthResult("", "", Date(), "", "")
            @Synchronized set(value) {
                field = value
            }
    }
}