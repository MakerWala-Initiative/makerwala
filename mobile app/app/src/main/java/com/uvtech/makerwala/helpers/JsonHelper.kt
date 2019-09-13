package com.uvtech.makerwala.helpers

import org.json.JSONObject

object JsonHelper {

    fun getString(jsonObject: JSONObject, key: String): String {
        if (jsonObject.has(key)) {
            try {
                return jsonObject.getString(key)
            } catch (e: Exception) {
                return ""
            }

        }
        return ""
    }

    fun getInt(jsonObject: JSONObject, key: String): Int {
        if (jsonObject.has(key)) {
            try {
                return jsonObject.getInt(key)
            } catch (e: Exception) {
                return 0
            }

        }
        return 0
    }

    fun getDouble(jsonObject: JSONObject, key: String): Double {
        if (jsonObject.has(key)) {
            try {
                return jsonObject.getDouble(key)
            } catch (e: Exception) {
                return 0.0
            }

        }
        return 0.0
    }

    fun getBoolean(jsonObject: JSONObject, key: String): Boolean {
        if (jsonObject.has(key)) {
            try {
                return jsonObject.getBoolean(key)
            } catch (e: Exception) {
                return false
            }

        }
        return false
    }
}
