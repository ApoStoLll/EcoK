package com.missclick.eco.main.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.missclick.eco.R
import kotlinx.android.synthetic.main.fragment_profile_post_info.*


class ProfilePostInfo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_post_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments?.getParcelable<PositiveItem>("arg")
        infoAction.text = item?.action.toString()
        infoScore.text = item?.score.toString()
        infoTime.text = item?.time.toString().split("'")[1]
    }

}
