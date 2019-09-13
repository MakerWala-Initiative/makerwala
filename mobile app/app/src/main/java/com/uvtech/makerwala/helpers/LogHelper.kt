package com.uvtech.makerwala.helpers

import android.util.Log

object LogHelper {

    fun e(message: String?) {
        Log.e("MakaerWala", message)
    }

    fun e(key: String, message: String?) {
        try {
            Log.e(key, message)
        } catch (e: Exception) {
        }
    }
}
