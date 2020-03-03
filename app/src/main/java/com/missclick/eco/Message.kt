package com.missclick.eco


class Message(message : ArrayList<String>){

    var code : Int
    var reason : String
    var body = ArrayList<String>()
    init{
        code =  message[0].split(" ")[1].toInt()
        reason =  message[0].split(" ")[2]
        body.addAll(message)
        body.removeAt(0)
    }

}