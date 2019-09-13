package com.uvtech.makerwala.api

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.WindowManager
import com.uvtech.makerwala.R

class LoadingHelper private constructor() {

    private var dialog: Dialog? = null
    private var mContext: Context? = null

    val isDialogShowing: Boolean
        get() = if (dialog != null && dialog!!.isShowing) {
            dialog!!.isShowing
        } else {
            false
        }

    fun show(mContext: Context) {
        if (checkProgressOpen())
            return
        this.mContext = mContext
        if ((mContext as AppCompatActivity).isFinishing)
            return
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null)

        dialog = Dialog(mContext, R.style.DialogTheme)
        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(view)
        if (dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        if (dialog != null) {
            if (!dialog!!.isShowing) {
                dialog!!.show()
            }
        }
    }

    fun dismiss() {
        if (dialog != null) {
            if (!(mContext as AppCompatActivity).isFinishing)
                dialog!!.dismiss()
        }
    }

    private fun checkProgressOpen(): Boolean {
        return dialog != null && dialog!!.isShowing
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: LoadingHelper? = null

        fun getInstance(): LoadingHelper {
            if (instance == null) {
                instance = LoadingHelper()
            }
            return instance as LoadingHelper
        }
    }
}
