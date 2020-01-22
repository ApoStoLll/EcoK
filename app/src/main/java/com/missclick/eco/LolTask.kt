package com.missclick.eco

import android.os.AsyncTask
import android.util.Log
import java.io.PrintWriter
import java.net.Socket

class LolTask : AsyncTask<Int, Unit, Unit>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg value: Int?): Unit? {
        try {
            val soc = Socket("95.158.11.238", 8080)
            Log.e("krash", "Connect")
            val writer = PrintWriter(soc.getOutputStream())
            when(value[0]){
                0 -> {
                    writer.write("0")
                    writer.flush()
                    writer.close()
                    soc.close()
                }
                1 -> {
                    writer.write("1")
                    writer.flush()
                    writer.close()
                    soc.close()
                }
                2 -> {
                    writer.write("2")
                    writer.flush()
                    writer.close()
                    soc.close()
                }
            }

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
    }
}