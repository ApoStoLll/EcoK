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
    var out : BufferedWriter
    var input : BufferedReader
    var soc : Socket
    init {
        soc = Socket(ip, port)
        out = BufferedWriter(OutputStreamWriter(soc.getOutputStream()))
        input = BufferedReader(InputStreamReader(soc.getInputStream()))
    }
    fun writeRequest(str : String, method: String){
        val requestLine = "$method $str HTTP/1.1\r\n"
        val host = "Host: $ip:$port\r\n"
        val request = requestLine + host
        write(request)
        val message = input.readLine()
        //out.close()
        //input.close()
        soc.close()
        Log.e("RESPONSE: ", message)
    }
    fun write(request: String){
        //val socket = Socket("192.168.0.135", 8080)
        Log.e("REQUEST: ", request)
        out.write(request)
        out.flush()
        soc.shutdownOutput()
        //Log.e("RESPONSE: ", recvString(soc.getInputStream()))
    }

    @Throws(IOException::class)
    fun recvString(input: InputStream): String {
        var szBuf = ""
        var ch = input.read()
        while (ch >= 0 && ch != '\n'.toInt()) {
            szBuf += ch.toChar()
            ch = input.read()
        }
        return szBuf
    }
}