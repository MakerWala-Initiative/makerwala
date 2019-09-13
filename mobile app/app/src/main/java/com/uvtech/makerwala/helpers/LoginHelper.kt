package com.uvtech.makerwala.helpers

import com.uvtech.makerwala.api.RetrofitClient
import com.uvtech.makerwala.prefs.SessionManager

class LoginHelper {

    var userData: User
        get() = if (SessionManager.getSessionString(USER_DATA, "")!!.isEmpty()) User() else RetrofitClient.gson.fromJson(SessionManager.getSessionString(USER_DATA, "")!!, User::class.java)
        set(user) = SessionManager.setSessionString(USER_DATA, RetrofitClient.gson.toJson(user))

    val isLoggedIn: Boolean
        get() = SessionManager.getSessionString(USER_DATA, "")!!.isNotEmpty()

    fun clearUserData() {
        SessionManager.setSessionString(USER_DATA, "")
    }

    fun updateProfile() {
        val user = userData

        userData = user
    }

    companion object {
        private var loginHelper: LoginHelper? = null
        const val USER_DATA = "USERDATA"

        val instance: LoginHelper
            get() {
                if (loginHelper == null) {
                    loginHelper = LoginHelper()
                }
                return loginHelper!!
            }
    }
}
