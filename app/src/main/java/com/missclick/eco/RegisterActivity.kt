package com.missclick.eco

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.Button


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        findViewById<Button>(R.id.button2).setOnClickListener({logIn()})
        findViewById<Button>(R.id.button3).setOnClickListener({signUp()})
    }
    fun logIn(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun signUp(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_holder,SignUp())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
