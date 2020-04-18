package com.missclick.eco


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.missclick.eco.main.profile.PositiveAdapter
import com.missclick.eco.main.profile.PositiveItem
import com.missclick.eco.main.profile.ProfileAdapter
import com.missclick.eco.main.profile.ProfileItem
import kotlinx.android.synthetic.main.fragment_profile_positive.*


class ProfilePositive : Fragment() {

    private val items = listOf(
        PositiveItem("Александр", "Пушкин"),
        PositiveItem("Михаил", "Лермонтов"),
        PositiveItem("Александр", "Блок"),
        PositiveItem("Николай", "Некрасов"),
        PositiveItem("Фёдор", "Тютчев"),
        PositiveItem("Сергей", "Есенин"),
        PositiveItem("Владимир", "Маяковский")
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
            items,
            object : PositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {

                }
            })
        PositiveActions.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL,false)
        PositiveActions.adapter = myAdapter
    }

}
