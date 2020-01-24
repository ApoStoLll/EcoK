package com.missclick.eco

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class HttpClient(val ip : String, val port : Int){
    var writer : PrintWriter
    var input : BufferedReader
    var soc : Socket
    init {
        soc = Socket(ip, port)
        writer = PrintWriter(soc.getOutputStream())
        input = BufferedReader(InputStreamReader(soc.getInputStream()));
    }
    fun writeRequest(str : String, method: String){
        Log.e("Request", "1")
        val request_line = "${method} ${str} HTTP/1.1\r\n"
        Log.e("Request", request_line)
        val host = "Host: ${ip}:${port}\r\n"
        Log.e("Request", host)
        val request = request_line + host
        this.writer.write(request)
        val serverWord = input.readLine() // ждём, что скажет сервер'
        Log.e("RESPONSE", "STARTING RESPONSE")
        Log.e("RESPONSE", serverWord)
        Log.e("RESPONSE", "End")
        writer.flush()
        writer.close()
        input.close()
        soc.close()
    }
    fun readResponse(){

    }
}