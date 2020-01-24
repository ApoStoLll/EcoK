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
        //findViewById<Button>(R.id.button2).setOnClickListener({logIn()})
        logi()


    }

     fun logi(){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder,LogIn())
            transaction.addToBackStack(null)
            transaction.commit()
        }

}
