package com.uvtech.makerwala.activities

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uvtech.makerwala.BuildConfig
import com.uvtech.makerwala.R
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        tvVersion.text = resources.getString(R.string.str_version_v_question, BuildConfig.VERSION_NAME)

        Glide.with(this).load(R.drawable.ic_logo).apply(RequestOptions().placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo)).into(ivLogo)

        try {
            val handler = Handler()
            handler.postDelayed({
                Utility.callNewActivity(this, MainActivity::class.java)
            }, 3000)
        } catch (e: Exception) {
            LogHelper.e("oncreateex", e.message)
        }
    }

    override fun onBackPressed() {
        Utility.killActivity(this)
    }
}
