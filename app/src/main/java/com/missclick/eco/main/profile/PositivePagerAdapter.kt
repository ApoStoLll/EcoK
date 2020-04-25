package com.missclick.eco.main.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PositivePagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        val profile = ProfilePositive()
        return when (position) {
            0 -> {
                bundle.putBoolean("arg",true)
                profile.arguments = bundle
                profile


            }
            else -> {
                bundle.putBoolean("arg",false)
                profile.arguments = bundle
                profile
            }
        }
    }
    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Positive"
            else -> {
                return "Negative"
            }
        }
    }

}