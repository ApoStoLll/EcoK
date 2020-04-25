package com.missclick.eco

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FtpManager(private val server : String,private val user : String, private val pass : String = "", private val port : Int = 21) {
    private val ftp = FTPClient()

    private fun conn(){
        ftp.connect(server, port)
        ftp.login(user, pass)
        ftp.enterLocalPassiveMode() // important!
        ftp.setFileType(FTP.BINARY_FILE_TYPE)
    }

    fun getImage(fileName : String, imageName: String, context : Context){
        if(BitmapFactory.decodeFile(context.filesDir.path + "/" + fileName) == null){
            conn()
            val filename = File(context.filesDir, fileName)
            val fos = FileOutputStream(filename)
            ftp.retrieveFile(imageName, fos)
            fos.close()
            ftp.logout()
            ftp.disconnect()
        }
    }

    fun uploadImage(path : File, imageName : String, username : String){
        conn()
        val fis =  FileInputStream(path)
        ftp.storeFile("/$username/$imageName", fis)
        Log.e("Username", username)
        fis.close()
        ftp.logout()
        ftp.disconnect()
    }

    fun deleteAllfiles(path : String){
        conn()
        for(file in ftp.listFiles(path))
            ftp.deleteFile(path + file.name)
        ftp.logout()
        ftp.disconnect()
    }
}