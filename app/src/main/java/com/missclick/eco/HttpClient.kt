package com.missclick.eco

import android.util.Log
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL
import java.util.*
import java.io.*
import java.net.CacheRequest
import android.system.Os.socket

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
        val message = input.readLine()
        //out.close()
        //input.close()
        soc.close()
        Log.e("RESPONSE: ", message)
        val ms = Message(message)
        return ms
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

    fun write(request: String){
        //val socket = Socket("192.168.0.135", 8080)
        Log.e("REQUEST: ", request)
        out.write(request)
        out.flush()
        soc.shutdownOutput()
        //Log.e("RESPONSE: ", recvString(soc.getInputStream()))
    }

}