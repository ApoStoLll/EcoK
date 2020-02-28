package com.missclick.eco

import android.util.Log
import java.util.regex.Pattern

class Message(message : String){
    val code : Int
    //val mes : String
    val body : String
    init{
        val pattern = Pattern.compile("[0-9]+([0-9]+)?")

        val matcher = pattern.matcher(message)
        val array = ArrayList<String>()
        while(matcher.find())
            array.add(matcher.group())
        code = array.get(2).toInt()
        val str = message.split("\r\n")
        Log.e("SIZE ", str.size.toString())
        if(str.size > 2)
            body = str[0]
        else body = " "
        Log.e("BODY ", body)
    }
}