package com.uvtech.makerwala.helpers

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.uvtech.makerwala.ApplicationLoader
import com.uvtech.makerwala.R
import com.uvtech.makerwala.interfaces.OnItemClickListener

object ToastHelper {

    fun displayInfo(message: String) {
        val inflater = ApplicationLoader.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.dialog_toast, null)

        val tvTitle = layout.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.text = message

        val toast = Toast(ApplicationLoader.getInstance().applicationContext)
        //        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    fun displayInfo(resid: Int) {
        val inflater = ApplicationLoader.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.dialog_toast, null)

        val tvTitle = layout.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.setText(resid)

        val toast = Toast(ApplicationLoader.getInstance().applicationContext)
        //        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    fun displayDialog(context: Context?, message: String, onItemClickListener: OnItemClickListener?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            onItemClickListener?.onItemClick(0)
        }
        if (!(context as AppCompatActivity).isFinishing)
            builder.create().show()
    }

    fun displayDialog(context: Context?, message: Int, onItemClickListener: OnItemClickListener?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            onItemClickListener?.onItemClick(0)
        }
        if (!(context as AppCompatActivity).isFinishing)
            builder.create().show()
    }

    fun displayDialog(context: Context, resourceId: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(resourceId)
        builder.setCancelable(false)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        if (!(context as AppCompatActivity).isFinishing)
            builder.create().show()
    }

    fun displayDialog(context: Context?, string: String?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(string)
        builder.setCancelable(false)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        if (!(context as AppCompatActivity).isFinishing)
            builder.create().show()
    }

    fun displayDialogOkCancel(context: Context?, message: String, onItemClickListener: OnItemClickListener?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(context.resources.getString(R.string.str_yes)) { dialog, _ ->
            dialog.dismiss()
            onItemClickListener?.onItemClick(0)
        }
        builder.setNegativeButton(context.resources.getString(R.string.str_no)) { dialog, _ ->
            dialog.dismiss()
        }
        if (!(context as AppCompatActivity).isFinishing)
            builder.create().show()
    }

    fun displayDialogOkCancel(context: Context?, message: Int, onItemClickListener: OnItemClickListener?) {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(context.resources.getString(R.string.str_yes)) { dialog, _ ->
            dialog.dismiss()
            onItemClickListener?.onItemClick(0)
        }
        builder.setNegativeButton(context.resources.getString(R.string.str_no)) { dialog, _ ->
            dialog.dismiss()
        }
        if (!(context as AppCompatActivity).isFinishing)
            builder.create().show()
    }
}
