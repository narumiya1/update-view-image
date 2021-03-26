package com.example.uploadandviewimage.fragment

import android.content.Context
import android.content.SharedPreferences

class Preff(val context: Context) {
    companion object{
        const val  PREEF = "USER_PREF"
    }

    val sharedPref = context.getSharedPreferences(PREEF, 0)

    fun setValues(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getValues(key: String): String? {
        return sharedPref.getString(key, "")
    }
}