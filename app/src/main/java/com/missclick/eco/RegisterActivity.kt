package com.missclick.eco

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent



class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
    fun LogIn(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
