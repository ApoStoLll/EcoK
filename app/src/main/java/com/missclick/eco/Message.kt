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

        //Log.e("SIZE ", str.size.toString())
        /*if(str.size > 2)
            body = str[2]
        else body = " "*/
        body = parseBody(message)
        Log.e("BODY ", body)
    }

    fun parseBody(message : String) : String{
        val str = message.split("\r\n")
        if(str.size <=2 )
            return ""
        else{
            var i = 0
            var res = ""
            for(item in str){
                if(i <= 2 )
                    continue
                res += str[i] + "\r\n"
                i++
            }
            return res
        }
    }
}