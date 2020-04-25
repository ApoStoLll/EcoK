package com.missclick.eco.main.profile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

import com.missclick.eco.R
import kotlinx.android.synthetic.main.fragment_profile_plus.*


class ProfilePlus : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        returnTransition = MaterialFadeThrough.create(requireContext())
        exitTransition = MaterialFadeThrough.create(requireContext())
        return inflater.inflate(R.layout.fragment_profile_plus, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = PositivePagerAdapter(childFragmentManager)
        viewpager.adapter = fragmentAdapter
        tab.setupWithViewPager(viewpager)

        Log.e("Plus","Im herePlus")
    }

}
