package com.missclick.eco

import android.util.Log
import java.net.Socket
import java.io.*


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
        //Log.e("RESPONSE 1", "KEK")
        while(true){
            kek = input.readLine() ?: break
            if(kek == "") break
            Log.e("While 1", kek)
            message.add(kek)
        }
        for(item in message){
            Log.e("RESPONSE 1", item)
        }
        //out.close()
        //input.close()
        soc.close()
        return Message(message)
    }

    fun getUserData(username : String){
        val str = writeRequest("/user_data?username=$username", "GET")
        Log.e("STROKA: ", str.toString())
        parseData(str).get("id")
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

    fun parseData(str: Message) : HashMap<String, String>{
        val data = HashMap<String, String>()
        val kek = str.body.split("\r\n")
        for(item in kek){
            Log.e("parse: ", item)
        }
        /*data.put("id", kek[0])
        data.put("username", kek[1])
        data.put("score", kek[2])
        data.put("image", kek[3])
        data.put("followers", kek[4])
        data.put("following", kek[5])*/
        return data
    }

    fun write(request: String){
        //val socket = Socket("192.168.0.135", 8080)
        Log.e("REQUEST: ", request)
        out.write(request)
        out.flush()
        soc.shutdownOutput()
        //Log.e("RESPONSE: ", recvString(soc.getInputStream()))
    }

}