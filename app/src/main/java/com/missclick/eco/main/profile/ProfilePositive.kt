package com.missclick.eco


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.profile.PositiveAdapter
import com.missclick.eco.main.profile.PositiveItem
import com.missclick.eco.main.profile.ProfileAdapter
import com.missclick.eco.main.profile.ProfileItem
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_positive.*


class ProfilePositive : Fragment() {

    private val actions = listOf(
        PositiveItem("Поднял бутылку", 10),
        PositiveItem("Поднял бумажку", 5),
        PositiveItem("Провел уборку", 25),
        PositiveItem("Посадил дерево", 100),
        PositiveItem("Добавил урну", 8),
        PositiveItem("Поднял окурок", 5),
        PositiveItem("Отсортровал мусор", 20)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_positive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myAdapter = PositiveAdapter(
            actions,
            object : PositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {
                    (activity as MainActivity).startMenu(3)
                    Log.e("Score", item.score.toString())
                }
            })
        PositiveActions.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL,false)
        PositiveActions.adapter = myAdapter
    }

}
