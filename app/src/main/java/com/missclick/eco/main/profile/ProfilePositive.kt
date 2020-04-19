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
import com.missclick.eco.main.profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_positive.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfilePositive : Fragment() {

    private val positive = listOf(
        PositiveItem(1,"Поднял бутылку", 10),
        PositiveItem(2,"Поднял бумажку", 5),
        PositiveItem(3,"Провел уборку", 25),
        PositiveItem(4,"Посадил дерево", 100),
        PositiveItem(5,"Добавил урну", 8),
        PositiveItem(6,"Поднял окурок", 5),
        PositiveItem(7,"Отсортровал мусор", 20)
    )
    private val negative = listOf(
        PositiveItem(-1,"Выкинул бутылку", -10),
        PositiveItem(-2,"Выкинул бумажку", -5)

    )
    private var actions = negative
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_positive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments!!.getBoolean("arg")) actions = positive
        val myAdapter = PositiveAdapter(
            actions,
            object : PositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {
                    (activity as MainActivity).startMenu(3)
                    requestToServer(item.id)

                }
            })
        PositiveActions.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL,false)
        PositiveActions.adapter = myAdapter
    }

    fun requestToServer(id : Int){
        GlobalScope.launch {
            val client = (activity as MainActivity).client
            withContext(Dispatchers.IO) {
                client.connect()
                client.doAction(id)
            }
        }
    }
}
