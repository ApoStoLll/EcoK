package com.missclick.eco

import android.content.Context
import android.util.Log
import com.missclick.eco.main.profile.PositiveItem
import java.net.Socket
import java.io.*

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
        val arr = answ.body[4].split("/")
        val image = ftp.getImage(arr[arr.size - 1], answ.body[4], context) //ЭТА ШТУКА КРАШНЕТСЯ ЕСЛИ ДОБАВИТЬ ЕЩЕ ПАПОК в папку

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
        val fileName = path.name
        ftp.deleteAllfiles("/$username/")
        ftp.uploadImage(path, fileName, username)
        writeRequest("/changeAvatar?image=/$username/$fileName&username=$username", "POST")
    }

    fun addProfilePost(item : PositiveItem, username: String){
        val id = item.id
        val score = item.score
        writeRequest("/addProfilePost?action=$id&username=$username&score=$score", "POST")
    }

    fun getProfilePost(username : String):List<PositiveItem>{
        val answ = writeRequest("/getProfilePost?username=$username", "POST")
        val actions:MutableList<PositiveItem> = mutableListOf()
        for(kek in answ.body){
            if(kek[0] == 'C' || kek[5] == '/') continue
            val item = Post().getItem(kek.split(',')[1].split(' ')[1].toInt(),kek.split(',')[2])
            Log.e(item.action,item.score.toString())
            actions.add(item)

        }
        actions.reverse()
        return actions
    }

    private fun write(request: String){
        Log.e("REQUEST: ", request)
        out.write(request)
        out.flush()
        soc.shutdownOutput()
    }


}