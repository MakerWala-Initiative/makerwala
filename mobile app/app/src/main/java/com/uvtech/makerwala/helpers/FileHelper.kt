package com.uvtech.makerwala.helpers

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileHelper {

    fun getRootCacheDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    fun getGenerateDateTimeFileName(context: Context, ext: String): String {
        val s = SimpleDateFormat("ddMMyyyyhhmmss", Locale.US)
        val strTime = s.format(Date())
        return (context.cacheDir.toString()
                + File.separator + strTime + "." + ext)
    }
}
