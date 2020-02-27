package com.missclick.eco

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private val client = HttpClient("95.158.11.238", 8080)
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_feed -> {
                feedMenu()
                conn("0", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_exercises -> {
                signUpMenu()
                conn("1", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                logInMenu()
                conn("2", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                signUpMenu()
                conn("3", "GET")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_etc -> {
                logInMenu()
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
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }


    private fun feedMenu(){ // лоигИн меню интерфейс
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_holder,Feed())
        transaction.addToBackStack(null)
        transaction.commit()
    }


    // for testing, delete it later
    private fun logInMenu(){ // лоигИн меню интерфейс
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_holder,LogIn())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun signUpMenu(){ // сигнАп меню интерфейс
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(LogIn())
        transaction.replace(R.id.fragment_holder,SignUp())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
