package com.missclick.eco

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FtpManager(val server : String, val user : String, val pass : String = "", val port : Int = 21) {
    val ftp = FTPClient()

    private fun conn(){
        ftp.connect(server, port)
        ftp.login(user, pass)
        ftp.enterLocalPassiveMode() // important!
        ftp.setFileType(FTP.BINARY_FILE_TYPE)
    }

    fun getImage(fileName : String, imageName: String, context : Context) : Bitmap{
        conn()
        val filename = File(context.filesDir, fileName)
        val fos = FileOutputStream(filename)
        ftp.retrieveFile(imageName, fos)
        var image = BitmapFactory.decodeFile(context.filesDir.path + "/" + fileName)
        image = Bitmap.createScaledBitmap(image, 400, 400, false)
        fos.close()
        ftp.logout()
        ftp.disconnect()
        return image
    }

    fun uploadImage(path : File, imageName : String, username : String){
        val fis =  FileInputStream(path)
        ftp.storeFile("/$username/$imageName", fis)
        Log.e("Username", username)
        fis.close()
        ftp.logout()
        ftp.disconnect()
    }
}