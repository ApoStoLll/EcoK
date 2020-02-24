package com.missclick.eco

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

import android.widget.Button
import kotlinx.android.synthetic.main.fragment_sign_up.*
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
                client.checkUser("1", "1")
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
                if(password.text.toString() == password_two.text.toString()) client.addUser(nickname.text.toString(),
                    name.text.toString(),password.text.toString(), email.text.toString())
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
