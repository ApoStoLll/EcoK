package com.missclick.eco

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.fragment_log_in.*

import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.nickname
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterActivity : AppCompatActivity(){

    val client = HttpClient("95.158.11.238", 8080)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        logInMenu() // по дефолту запускаем ЛогИн меню
        // сделать проверку, если авторизован, то входим логИн()
    }

    private fun logInMenu(){ // лоигИн меню интерфейс
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_holder,LogIn())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun signUpMenu(){ // cbuyfg меню интерфейс
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(LogIn())
        transaction.replace(R.id.fragment_holder,SignUp())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun logIn(){ // вход
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                client.connect()
                //client.checkUser(nickname_logIn.text.toString(), password_logIn.text.toString())
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // переходим в мэин на свой акк
    }

    fun signUp(){ // регистрация
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                client.connect()
                if(password_signUp.text.toString() == password_two.text.toString()) client.addUser(nickname_signUp.text.toString(),
                    name.text.toString(),password_signUp.text.toString(), email.text.toString())
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
