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

    private fun writeRequest(str : String, method: String) : Message{
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
    fun getImage(imagePath:String,context: Context):String{
        val arr = imagePath.split("/")
        val imageName = arr[arr.size - 1]
        ftp.getImage(imageName,imagePath,context)
        if (!soc.isClosed) soc.close()
        return imageName//ЭТА ШТУКА КРАШНЕТСЯ ЕСЛИ ДОБАВИТЬ ЕЩЕ ПАПОК в папку
    }
    fun getUserData(username : String, context : Context) : User {
        val answ = writeRequest("/user_data?username=$username", "GET")
        val imageName = getImage(answ.body[4], context)
        val followers:MutableList<String> = mutableListOf()
        for(follower in answ.body[5].split("_")){
            if(follower == "1") continue
            followers.add(follower)
        }
        val followings:MutableList<String> = mutableListOf()
        for(following in answ.body[6].split("_")){
            if(following == "1") continue
            followings.add(following)
        }
        return  User(username, answ.body[1], answ.body[3], imageName, followers, followings)
    }

//     fun newGetUserData(username : String, context : Context) : User {
//        val answ = writeRequest("/user_data?username=$username", "GET")
//        val imageName = getImage(answ.body[4], context)
//        val followers:MutableList<String> = mutableListOf()
//        for(follower in answ.body[5].split("_")){
//            if(follower == "1") continue
//            followers.add(follower)
//        }
//        val followings:MutableList<String> = mutableListOf()
//        for(following in answ.body[6].split("_")){
//            if(following == "1") continue
//            followings.add(following)
//        }
//        return  User(username, answ.body[1], answ.body[3], imageName, followers, followings)
//    }

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

    fun uploadImage(path: File, username : String,post: Boolean){
        val fileName = path.name
        //ftp.deleteAllfiles("/$username/")
        ftp.uploadImage(path, fileName, username)
        if(!post) writeRequest("/changeAvatar?image=/$username/$fileName&username=$username", "POST")
        else soc.close()
    }

    fun addProfilePost(item : PositiveItem, username: String){
        val id = item.id
        val score = item.score
        var description = item.description
        if(description == "") description = "NULL"
        val share = item.share
        var imageName = item.imageName
        imageName = if(imageName != "") "/$username/$imageName" else "NULL"
        writeRequest("/addProfilePost?action=$id&username=$username&score=$score&share=$share&image=$imageName&text=$description", "POST")
    }

    fun getProfilePost(username : String):List<PositiveItem>{
        val answ = writeRequest("/getProfilePost?username=$username", "POST")
        val actions:MutableList<PositiveItem> = mutableListOf()
        for(kek in answ.body){
            if(kek[0] == 'C') continue
            val arr = kek.split(',')
            val imageName = arr[4].split("'")[1]
            val item = Post().getItem(arr[1].split(' ')[1].toInt(),arr[2],imageName,arr[5],arr[6])
            actions.add(item)

        }
        actions.reverse()
        return actions
    }

    fun getAllNicknames():List<String>{
        val answ = writeRequest("/getAllNicknames", "POST")
        val nicknames:MutableList<String> = mutableListOf()
        for(nick in answ.body) {
            if (nick[0] == 'C') continue
            nicknames.add(nick.split("'")[1])
        }
        return nicknames
    }

    fun follow(from: String,to: String){
        writeRequest("/follow?from=$from&to=$to", "POST")
    }

    fun unfollow(from: String,to: String){
        writeRequest("/unfollow?from=$from&to=$to", "POST")
    }

    private fun write(request: String){
        Log.e("REQUEST: ", request)
        out.write(request)
        out.flush()
        soc.shutdownOutput()
    }


}