package com.uvtech.makerwala.prefs

import android.content.Context
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.prefs.AppPrefData

object SessionManager {

    fun setSessionString(key: String, value: String) {
        val editor = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getSessionString(key: String, defaultValue: String): String? {
        val sharedPreferences = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }


    fun setSessionInt(key: String, value: Int) {
        val editor = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getSessionInt(key: String, defaultValue: Int): Int {
        val sharedPreferences = ApplicationLoader.getInstance()
                .getSharedPreferences(AppPrefData.PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, defaultValue)
    }
}
