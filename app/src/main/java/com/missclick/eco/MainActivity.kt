package com.missclick.eco

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    val client = HttpClient("95.158.11.238", 8080)
    lateinit var user : User
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_feed -> {
                textMessage.setText(R.string.title_feed)
                conn("0", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_exercises -> {
                textMessage.setText(R.string.title_exercises)
                conn("1", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                textMessage.setText(R.string.title_profile)
                conn("2", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                textMessage.setText(R.string.title_map)
                conn("3", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_etc -> {
                textMessage.setText(R.string.title_etc)
                conn("4", "GET")
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

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }
}
