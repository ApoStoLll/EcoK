package com.missclick.eco

import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.media.ImageReader
import android.util.Log
import java.net.Socket
import java.io.*
import android.graphics.Bitmap
import android.os.Environment
import android.util.Base64
import android.widget.ImageView
import com.missclick.eco.main.MainActivity
import com.missclick.eco.register.RegisterActivity
import com.squareup.picasso.Picasso
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.apache.commons.net.ftp.FTP
import com.squareup.picasso.Target

class HttpClient(private val ip : String,private val port : Int){
    private lateinit var out : BufferedWriter
    private lateinit var input : BufferedReader
    private lateinit var soc : Socket
    private lateinit var ftp : FtpManager

    fun connect(){
        soc = Socket(ip, port)
        out = BufferedWriter(OutputStreamWriter(soc.getOutputStream()))
        input = BufferedReader(InputStreamReader(soc.getInputStream()))
        ftp = FtpManager("95.158.11.238", "kek")
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

    fun getUserData(username : String, context : Context) : User {
        val answ = writeRequest("/user_data?username=$username", "GET")
        val image = ftp.getImage("ava.png", answ.body[4], context)
        return  User(username, answ.body[1], answ.body[3], image, answ.body[5], answ.body[6])
    }

    fun addUser(username : String, name : String, pass : String, email : String) : Boolean{
        val answ = writeRequest("/users?username=$username&name=$name&password=$pass&email=$email", "POST")
        if(answ.code == 204) return true
        return false
    }

    fun checkUser(username : String, password : String) : Boolean{
        val answ = writeRequest("/user?username=$username&password=$password", "POST")
        if(answ.code == 200) return true
        return false
    }

    fun uploadImage(path: File, username : String){
        //val fileName = path.name
        ftp.uploadImage(path, "ava.png", username)
        writeRequest("/changeAvatar?image=/$username/ava.png&username=$username", "POST")
    }
    private fun write(request: String){
        Log.e("REQUEST: ", request)
        out.write(request)
        out.flush()
        soc.shutdownOutput()
    }


}