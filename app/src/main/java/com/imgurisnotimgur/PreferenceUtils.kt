package com.imgurisnotimgur

class PreferenceUtils {
    companion object {
        val sectionEntries = arrayOf (
            "Hot",
            "Top",
            "User"
        )

        val sortEntries = arrayOf (
            "Viral",
            "Time",
            "Top (today)",
            "Top (this week)",
            "Top (this month)",
            "Top (this year)",
            "Top (all time)"
        )

        fun findIndexOfValue(values: Array<String>, value: String): Int {
            for (i in values.indices) {
                if (values[i].equals(value)) {
                    return i
                }
            }

            return 0
        }
    }
}