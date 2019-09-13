package com.uvtech.makerwala.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uvtech.makerwala.R
import com.uvtech.makerwala.activities.ChangePasswordActivity
import com.uvtech.makerwala.activities.MainActivity
import com.uvtech.makerwala.activities.ProfileActivity
import com.uvtech.makerwala.helpers.LoginHelper
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FragmentSettings : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tvTitle!!.text = resources.getString(R.string.str_settings)
        view.llLeft!!.visibility = View.INVISIBLE

        view.rlProfile.setOnClickListener { Utility.callActivity(context!!, ProfileActivity::class.java) }
        view.rlChangePassword.setOnClickListener { Utility.callActivity(context!!, ChangePasswordActivity::class.java) }
        view.rlLogOut.setOnClickListener {
            LoginHelper.instance.clearUserData()
            Utility.callNewActivity(context!!, MainActivity::class.java)
        }
    }
}
