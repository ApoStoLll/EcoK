package com.missclick.eco.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import com.missclick.eco.main.profile.Profile
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    lateinit var nickname : String

    val client = HttpClient("95.158.11.238", 8080)
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_feed -> {
                startMenu(1)
                conn("0", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_exercises -> {
                startMenu(2)
                conn("1", "GET")
                Log.d("Nick: ", nickname)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                startMenu(3)
                conn("2", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                startMenu(4)
                conn("3", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_etc -> {
                startMenu(5)
                    GlobalScope.launch {
                        withContext(Dispatchers.IO){
                            client.connect()
                            //client.getUserData("aloxa",)
                        }
                        Log.e("COROUTINE", "DONE")

                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun conn(message : String, method : String){
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
        val nickname:String = intent.getStringExtra("nickname")
        this@MainActivity.nickname = nickname
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        getSupportActionBar()?.hide()
        startMenu(1)
    }


    fun startMenu(num : Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(num){
            1-> transaction.replace(R.id.fragment_holder, Feed())
            2-> transaction.replace(R.id.fragment_holder, Exercises())
            3-> transaction.replace(R.id.fragment_holder, Profile())
            4-> transaction.replace(R.id.fragment_holder, Map())
            5-> transaction.replace(R.id.fragment_holder, Etc())
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
