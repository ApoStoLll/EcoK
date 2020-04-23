package com.missclick.eco.main.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.missclick.eco.ProfilePositive
import com.missclick.eco.main.Etc
import com.missclick.eco.main.Exercises
import com.missclick.eco.main.Feed

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
            1 -> "Negative"
            else -> {
                return "Null"
            }
        }
    }

}