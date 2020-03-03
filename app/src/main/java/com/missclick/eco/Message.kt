package com.missclick.eco


data class Message(val message : ArrayList<String>){

    var code =  message[0].split(" ")[1].toInt()
    var reason = message[0].split(" ")[2]
    var body = ArrayList<String>()
    init{
        body.addAll(message)
        body.removeAt(0)
    }
}