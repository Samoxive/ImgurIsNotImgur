package com.imgurisnotimgur

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
    }
}