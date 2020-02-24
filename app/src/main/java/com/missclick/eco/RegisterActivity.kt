package com.missclick.eco

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

import android.widget.Button


class RegisterActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        logInMenu()
        // проверка, если авторизован, то входим логИн()
    }

    private fun logInMenu(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_holder,LogIn())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun signUpMenu(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(LogIn())
        transaction.replace(R.id.fragment_holder,SignUp())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun logIn(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // переходим в мэин на свой акк
    }

    fun signUp(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
