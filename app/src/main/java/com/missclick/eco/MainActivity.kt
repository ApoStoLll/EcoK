package com.missclick.eco

import android.content.Intent
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
    val client = HttpClient("95.158.11.238", 8080)
    lateinit var user : User
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                conn("0", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                conn("1", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                conn("2", "GET")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun conn(message : String, method : String){
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                client.connect()
                client.writeRequest(message, method)
            }
            Log.e("COROUTINE", "DONE")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        user = User()
        if(!user.isAuthorizate){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }
}
