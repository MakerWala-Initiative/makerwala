package com.uvtech.makerwala.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationConstants.ON_ACTIVITY_RESULT_VIDEO_DETAIL_TO_DOWNLOAD
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.BuildConfig
import com.uvtech.makerwala.R
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.fragments.management.FragmentTabDownload
import com.uvtech.makerwala.fragments.management.FragmentTabHome
import com.uvtech.makerwala.fragments.management.FragmentTabProfile
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.helpers.ToastHelper
import com.uvtech.makerwala.models.HomeListModel
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentTabHome: FragmentTabHome
    private lateinit var fragmentTabProfile: FragmentTabProfile
    private lateinit var fragmentTabDownload: FragmentTabDownload

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fragmentTabHome = supportFragmentManager.findFragmentById(R.id.fragmentTabHome) as FragmentTabHome
        fragmentTabProfile = supportFragmentManager.findFragmentById(R.id.fragmentTabProfile) as FragmentTabProfile
        fragmentTabDownload = supportFragmentManager.findFragmentById(R.id.fragmentTabLocal) as FragmentTabDownload

        llHome.setOnClickListener {
            setTabSelected(1)
        }

        llProfile.setOnClickListener {
            if (LoginHelper.instance.isLoggedIn) setTabSelected(2) else Utility.callActivity(
                this,
                LoginActivity::class.java
            )
        }

        llLocal.setOnClickListener {
            setTabSelected(3)
        }
        setTabSelected(1)
        fetch()
    }

    private fun create() {
        val builder = Uri.Builder()
        builder.scheme(getString(R.string.config_scheme))
            .authority(getString(R.string.config_host))
            .appendQueryParameter("userid", "3")
        val uri = builder.build()

        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(uri)
            .setDynamicLinkDomain("makerwala.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(packageName)
                    .setMinimumVersion(BuildConfig.VERSION_CODE)
                    .build()
            )
//                    .setIosParameters(
//                            DynamicLink.IosParameters.Builder("com.Finlitetech.LiveKeeping")
//                                    .setAppStoreId("1451277319")
//                                    .setMinimumVersion("1.0.10")
//                                    .build())
            .setNavigationInfoParameters(
                DynamicLink.NavigationInfoParameters.Builder()
                    .setForcedRedirectEnabled(true).build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener { shortDynamicLink ->
                val mInvitationUrl = shortDynamicLink.shortLink
                LogHelper.e(mInvitationUrl.toString())
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "$mInvitationUrl")
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            }
            .addOnFailureListener {
                LogHelper.e(it.message)
            }
    }

    private fun fetch() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                if (pendingDynamicLinkData != null) {
                    try {
                        val deepLink = pendingDynamicLinkData.link
                        LogHelper.e("success $deepLink")

                        val homeModel = HomeListModel(
                            deepLink.getQueryParameter(WebFields.VIDEO_ID).toInt(),
                            deepLink.getQueryParameter(WebFields.YOUTUBE_LINK),
                            deepLink.getQueryParameter(WebFields.VIDEO_TITLE),
                            deepLink.getQueryParameter(WebFields.PUBLIC_DETAIL),
                            deepLink.getQueryParameter(WebFields.PRIVATE_DETAIL),
                            deepLink.getQueryParameter(WebFields.RATING).toFloat(),
                            deepLink.getQueryParameter(WebFields.IS_PRIVATE).toBoolean()
                        )

                        ApplicationLoader.getInstance().homeListModel = homeModel
                        Utility.callActivityForResult(
                            this, VideoDetailsActivity::class.java,
                            ON_ACTIVITY_RESULT_VIDEO_DETAIL_TO_DOWNLOAD
                        )
                    } catch (e: Exception) {
                        LogHelper.e(e.message)
                    }
                }
            }
            .addOnFailureListener {
                LogHelper.e(it.message)
            }
    }

    private fun setTabSelected(index: Int) {
        llOneContainer.visibility = View.GONE
        llTwoContainer.visibility = View.GONE
        llThreeContainer.visibility = View.GONE
        ivHome.setImageResource(R.drawable.ic_action_home)
        tvHome.setTextColor(resources.getColor(R.color.colorWhite))
        ivProfile.setImageResource(if (LoginHelper.instance.isLoggedIn) R.drawable.ic_action_settings else R.drawable.ic_action_profile)
        tvProfile.setTextColor(resources.getColor(R.color.colorWhite))
        ivLocal.setImageResource(R.drawable.ic_action_folder)
        tvDownload.setTextColor(resources.getColor(R.color.colorWhite))
        when (index) {
            1 -> {
                ivHome.setImageResource(R.drawable.ic_action_home_active)
                tvHome.setTextColor(resources.getColor(R.color.colorAccent))
                llOneContainer.visibility = View.VISIBLE
                ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION,"Home")
            }
            2 -> {
                ivProfile.setImageResource(if (LoginHelper.instance.isLoggedIn) R.drawable.ic_action_settings_active else R.drawable.ic_action_profile_active)
                tvProfile.setTextColor(resources.getColor(R.color.colorAccent))
                llTwoContainer.visibility = View.VISIBLE
                ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION,"Settings")
            }
            3 -> {
                ivLocal.setImageResource(R.drawable.ic_action_folder_active)
                tvDownload.setTextColor(resources.getColor(R.color.colorAccent))
                llThreeContainer.visibility = View.VISIBLE
                fragmentTabDownload.fragmentDownload.setAutoStartDownload()
                ApplicationLoader.getInstance().callActivityLog(WebFields.INFORMATION,"Download")
            }
        }
    }

    private fun addFragment(fragment: Fragment, res: Int) {
        val bundle = Bundle()
        fragment.arguments = bundle
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(res, fragment, "fragment")
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (llOneContainer.visibility == View.VISIBLE && fragmentTabHome != null)
            if (fragmentTabHome.onBackPressed()) {
                return
            }
        if (llTwoContainer.visibility == View.VISIBLE && fragmentTabProfile != null)
            if (fragmentTabProfile.onBackPressed()) {
                return
            }
        if (llThreeContainer.visibility == View.VISIBLE && fragmentTabDownload != null)
            if (fragmentTabDownload.onBackPressed()) {
                return
            }
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true
            ToastHelper.displayInfo(R.string.str_please_click_back_again_to_exit)

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            return
        }
        super.onBackPressed()
    }

    var doubleBackToExitPressedOnce = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == ApplicationConstants.ON_ACTIVITY_RESULT_VIDEO_DETAIL_TO_DOWNLOAD)
                setTabSelected(3)
    }
}
