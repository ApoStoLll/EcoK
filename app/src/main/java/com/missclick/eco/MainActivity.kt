package com.missclick.eco

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import kotlin.math.log
import javax.xml.datatype.DatatypeConstants.SECONDS
import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


//kek

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                conn(0)
                //LolTask().execute(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                conn(1)
                //LolTask().execute(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                //LolTask().execute(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    /*fun conn(str: String){
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
    }*/

    fun conn(code : Int){
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                val client = HttpClient("192.168.0.135", 8080)
                client.writeRequest("0", "GET")
            }
            Log.e("COROUTINE", "DONE")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }
}
