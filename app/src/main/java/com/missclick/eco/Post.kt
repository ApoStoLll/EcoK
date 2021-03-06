package com.missclick.eco

import android.util.Log
import com.missclick.eco.main.profile.PositiveItem


class Post{
    private val actions = mapOf(
        1 to PositiveItem(1,"Поднял бутылку", 10),
        2 to PositiveItem(2,"Поднял бумажку", 5),
        3 to PositiveItem(3,"Провел уборку", 25),
        4 to PositiveItem(4,"Посадил дерево", 100),
        5 to PositiveItem(5,"Добавил урну", 8),
        6 to PositiveItem(6,"Поднял окурок", 5),
        7 to PositiveItem(7,"Отсортровал мусор", 20),
        -1 to PositiveItem(-1,"Выкинул бутылку", -10),
        -2 to PositiveItem(-2,"Выкинул бумажку", -5)
    )
    fun getItem(id:Int,time: String,imageName : String, description : String, share:String): PositiveItem {
        val item = actions.getValue(id)
        item.time = time.split("'")[1]
        Log.e("desk",description.split("'")[1])
        item.description = description.split("'")[1]
        item.share = share.split("'")[1].toBoolean()
        item.imageName = imageName
        return item
    }
    fun getAction(id:Int):String{
        return actions.getValue(id).action
    }

    fun getScore(id: Int):Int{
        return actions.getValue(id).score
    }
}