package com.missclick.eco

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import kotlin.math.log
import javax.xml.datatype.DatatypeConstants.SECONDS
import android.os.AsyncTask



class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                //conn("Home")
                val mt = MyTask()
                mt.execute()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                //conn("DashBoard")
                val mt = MyTask()
                mt.execute()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun conn(str: String){
        Log.e("krash", "AFJHEFHIEUFHIEUFHNKEFNAIUEFNAUENFJKAE")
        try{
            val soc = Socket("95.158.11.238", 8080)
            Log.e("krash", "Connect")
            val writer = PrintWriter(soc.getOutputStream())
            writer.write(str)
            writer.flush()
            writer.close()
            soc.close()
        }catch (e: Exception){
            Log.e("krash", "bol" + e.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }
    internal inner class MyTask : AsyncTask<Unit, Unit, Unit>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Unit): Unit? {
            try {
                val soc = Socket("95.158.11.238", 8080)
                Log.e("krash", "Connect")
                val writer = PrintWriter(soc.getOutputStream())
                writer.write("hellp")
                writer.flush()
                writer.close()
                soc.close()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
        }
    }
}
