package com.missclick.eco

import android.graphics.BitmapFactory
import android.media.Image
import android.media.ImageReader
import android.util.Log
import java.net.Socket
import java.io.*
import android.graphics.Bitmap
import android.util.Base64


class HttpClient(val ip : String, val port : Int){
    lateinit var out : BufferedWriter
    lateinit var input : BufferedReader
    lateinit var soc : Socket

    fun connect(){
        soc = Socket(ip, port)
        out = BufferedWriter(OutputStreamWriter(soc.getOutputStream()))
        input = BufferedReader(InputStreamReader(soc.getInputStream()))
    }

    fun writeRequest(str : String, method: String) : Message{
        val requestLine = "$method $str HTTP/1.1\r\n"
        val host = "Host: $ip:$port\r\n"
        val request = requestLine + host
        write(request)
        val message = ArrayList<String>()
        var kek : String
        while(true){
            kek = input.readLine() ?: break
            if(kek == "") break
            message.add(kek)
        }
        for(item in message){
            Log.e("RESPONSE 1", item)
        }
        soc.close()
        return Message(message)
    }

    fun decodeBase64(input: String): Bitmap {
        val decodedBytes = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getUserData(username : String) : User{
        val answ = writeRequest("/user_data?username=$username", "GET")
        var str = answ.body[4]
        str = str.substring(2, str.length-2)
        //Log.e("STR", str[str.length-1].toString())
        val img = decodeBase64(str)//str.toByteArray()
        //Log.e("STR", byte.toString())
        //val img = BitmapFactory.decodeByteArray(byte, 0, str.length)
        return  User(username, answ.body[1], answ.body[3], img, answ.body[5], answ.body[6])
    }

    fun addUser(username : String, name : String, pass : String, email : String) : Boolean{
        val answ = writeRequest("/users?username=$username&name=$name&password=$pass&email=$email", "POST")
        if(answ.code == 204) return true
        return false
    }

    fun checkUser(username : String, password : String) : Boolean{
        val answ = writeRequest("/user?username=$username&password=$password", "POST")
        Log.e("RESPONSE: ", "CODE: " + answ.code)
        if(answ.code == 200) return true
        return false
    }

    private fun write(request: String){
        Log.e("REQUEST: ", request)
        out.write(request)
        out.flush()
        soc.shutdownOutput()
    }

}