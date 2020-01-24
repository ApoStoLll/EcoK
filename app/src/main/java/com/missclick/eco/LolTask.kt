package com.missclick.eco

import android.os.AsyncTask
import android.util.Log

class LolTask : AsyncTask<Int, Unit, Unit>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg value: Int?): Unit? {
        try {
            //val soc = Socket("95.158.11.238", 8080)
            Log.e("krash", "Connect")
            //val writer = PrintWriter(soc.getOutputStream())
            val client = HttpClient("95.158.11.238", 8080)
            when(value[0]){
                0 -> {
                    client.writeRequest("0", "GET")
                }
                1 -> {
                    client.writeRequest("1", "GET")
                }
                2 -> {
                    client.writeRequest("2", "GET")
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