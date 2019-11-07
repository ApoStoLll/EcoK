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

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        Log.e("krash", "AFJHEFHIEUFHIEUFHNKEFNAIUEFNAUENFJKAE")
        try{
            val soc = Socket("192.168.0.103", 53210)
            Log.e("krash", "Connect")
            val writer = PrintWriter(soc.getOutputStream())
            writer.write("kek")
            writer.flush()
            writer.close()
            soc.close()
        }catch (e: Exception){
            Log.e("krash", "bol")
        }
    }
}
