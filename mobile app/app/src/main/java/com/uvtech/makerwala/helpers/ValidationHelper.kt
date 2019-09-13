package com.uvtech.makerwala.helpers

import android.util.Patterns

object ValidationHelper {

    private const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+"
    private const val REGEX_PATTERN_GST = "[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}"

    fun isValidEmail(email: String): Boolean {
        return !email.matches(EMAIL_PATTERN.toRegex())
    }

    fun isValidMobileNumber(mobile: String): Boolean {
        return mobile.length != 10
    }

    fun isValidPhoneNumber(mobile: String): Boolean {
        return mobile.length != 8
    }

    fun isValidatePassword(password: String): Boolean {
        return password.length != 6
    }

    fun isPasswordAtLeastSixChar(password: String): Boolean {
        return password.length < 6
    }

    fun isGstNumber(gst: String): Boolean {
        return !gst.matches(REGEX_PATTERN_GST.toRegex())
    }
}
