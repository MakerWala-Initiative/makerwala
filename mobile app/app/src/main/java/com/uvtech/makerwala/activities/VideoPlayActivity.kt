package com.uvtech.makerwala.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.utilities.EncryptDecrypt
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_video_play.*
import java.io.File

class VideoPlayActivity : AppCompatActivity() {

    private lateinit var videoFile: File
    private lateinit var encryptFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video_play)

        val model = ApplicationLoader.getInstance().downloadedModel
        LogHelper.e(ApplicationLoader.getInstance().downloadedModel.toString())

        videoFile = File(
            cacheDir,
            "temp.${model.videoExtension}"
        )
        encryptFile = File(model.videoFileName)
        EncryptDecrypt.decode(encryptFile.absolutePath, videoFile.absolutePath)


        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                and View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                and View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                and View.SYSTEM_UI_FLAG_IMMERSIVE)

        player.setSource(Uri.fromFile(videoFile))
        player.setAutoPlay(true)
    }

    override fun onResume() {
        super.onResume()
        player.start()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onBackPressed() {
        Utility.killActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (videoFile.exists()) videoFile.delete()
        player.release()
    }
}
