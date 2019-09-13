package com.uvtech.makerwala.fragments.management

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uvtech.makerwala.R
import com.uvtech.makerwala.fragments.FragmentSettings

class FragmentTabProfile : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_management, container, false)
    }

    lateinit var fragmentSettings: FragmentSettings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSettings = FragmentSettings()
        addFragment(fragmentSettings)
    }

    fun addFragment(fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.llContainer, fragment, "fragment")
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun addFragmentAnim(fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.llContainer, fragment, "fragment")
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return true
        }
        return false
    }
}
