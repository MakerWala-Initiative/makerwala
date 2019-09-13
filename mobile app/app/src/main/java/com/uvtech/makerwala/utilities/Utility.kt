package com.uvtech.makerwala.utilities

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.uvtech.makerwala.R
import java.util.*

object Utility {

    fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
    }

    private fun getBytesToMBString(bytes: Long): String {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }

    fun callActivity(context: Context, myIntent: Intent) {
        val bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        context.startActivity(myIntent, bundle)
    }

    fun callActivity(context: Context, classes: Class<*>) {
        val myIntent = Intent(context, classes)
        val bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        context.startActivity(myIntent, bundle)
    }

    fun callActivity(context: Context, classes: Class<*>, key: String, value: String) {
        val myIntent = Intent(context, classes)
        myIntent.putExtra(key, value)
        val bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        context.startActivity(myIntent, bundle)
    }

    fun callActivity(context: Context, classes: Class<*>, key: String, value: String,
                     key1: String, value1: String,
                     key2: String, value2: String) {
        val myIntent = Intent(context, classes)
        myIntent.putExtra(key, value)
        myIntent.putExtra(key1, value1)
        myIntent.putExtra(key2, value2)
        val bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        context.startActivity(myIntent, bundle)
    }

    fun callActivity(context: Context, classes: Class<*>, key: String, value: Int) {
        val myIntent = Intent(context, classes)
        myIntent.putExtra(key, value)
        val bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        context.startActivity(myIntent, bundle)
    }

    fun callActivity(context: Context, classes: Class<*>, key: String, value: String, key1: String, value1: String) {
        val myIntent = Intent(context, classes)
        myIntent.putExtra(key, value)
        myIntent.putExtra(key1, value1)
        val bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        context.startActivity(myIntent, bundle)
    }

    fun callNewActivity(context: Context, classes: Class<*>) {
        val myIntent = Intent(context, classes)
        myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bundle = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        context.startActivity(myIntent, bundle)
    }

    @SuppressLint("RestrictedApi")
    fun callActivityForResult(appCompatActivity: AppCompatActivity, classes: Class<*>, code: Int) {
        val intent = Intent(appCompatActivity, classes)

        val bundle = ActivityOptions.makeCustomAnimation(appCompatActivity, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        appCompatActivity.startActivityForResult(intent, code, bundle)
    }

    @SuppressLint("RestrictedApi")
    fun callActivityForResult(appCompatActivity: AppCompatActivity, classes: Class<*>, code: Int,
                              key1: String, value1: String, key2: String, value2: String,
                              key3: String, value3: String, key4: String, value4: String,
                              key5: String, value5: String, key6: String, value6: String,
                              key7: String, value7: String, key8: String, value8: String) {
        val intent = Intent(appCompatActivity, classes)
        intent.putExtra(key1, value1)
        intent.putExtra(key2, value2)
        intent.putExtra(key3, value3)
        intent.putExtra(key4, value4)
        intent.putExtra(key5, value5)
        intent.putExtra(key6, value6)
        intent.putExtra(key7, value7)
        intent.putExtra(key8, value8)

        val bundle = ActivityOptions.makeCustomAnimation(appCompatActivity, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        appCompatActivity.startActivityForResult(intent, code, bundle)
    }

    @SuppressLint("RestrictedApi")
    fun callActivityForResult(appCompatActivity: AppCompatActivity, classes: Class<*>, code: Int,
                              key1: String, value1: String, key2: String, value2: String,
                              key3: String, value3: String, key4: String, value4: String,
                              key5: String, value5: String, key6: String, value6: String) {
        val intent = Intent(appCompatActivity, classes)
        intent.putExtra(key1, value1)
        intent.putExtra(key2, value2)
        intent.putExtra(key3, value3)
        intent.putExtra(key4, value4)
        intent.putExtra(key5, value5)
        intent.putExtra(key6, value6)

        val bundle = ActivityOptions.makeCustomAnimation(appCompatActivity, R.anim.slide_in_left, R.anim.slide_out_right).toBundle()
        appCompatActivity.startActivityForResult(intent, code, bundle)
    }

    fun killActivity(appCompatActivity: AppCompatActivity?) {
        if (appCompatActivity != null) {
            appCompatActivity.finish()
            appCompatActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceToken(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun hideKeyboard(context: Context, view: View?) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun checkPlayServices(context: Context): Boolean {
        val status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)
        return if (status != ConnectionResult.SUCCESS) {
            false
        } else true
    }
}
