package com.missclick.eco

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileOutputStream

class FtpManager(val server : String, val user : String, val pass : String = "", val port : Int = 21) {
    val ftp = FTPClient()

    fun getImage(fileName : String, imageName: String, context : Context) : Bitmap{
        ftp.connect(server, port)
        ftp.login(user, pass)
        ftp.enterLocalPassiveMode() // important!
        ftp.setFileType(FTP.BINARY_FILE_TYPE)
        val filename = File(context.filesDir,fileName)
        val fos = FileOutputStream(filename)
        ftp.retrieveFile(imageName, fos)
        var image = BitmapFactory.decodeFile(context.filesDir.path + "/" + fileName)
        image = Bitmap.createScaledBitmap(image, 400, 400, false)
        fos.close()
        ftp.logout()
        ftp.disconnect()
        return image
    }
}