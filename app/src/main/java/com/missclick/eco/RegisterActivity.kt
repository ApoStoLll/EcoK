package com.missclick.eco

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*


class RegisterActivity : AppCompatActivity(){

    val client = HttpClient("95.158.11.238", 8080)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // create upload of file
        //if(readFile("isAuth")=="true") startMain()
        logInMenu() // по дефолту запускаем ЛогИн меню
    }

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

    fun logIn(){
        var isAuth = false
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                client.connect()
                if (client.checkUser(nickname_logIn.text.toString(), password_logIn.text.toString())){
                    runOnUiThread(){
                        startMain()
                    }
                }
            }

        }
        if(isAuth)startMain()
        writeFile(nickname_logIn.text.toString(), password_logIn.text.toString(),true)
        if(readFile("isAuth")=="True") startMain()
        // переходим в мэин на свой акк
    }

    fun signUp(){ // регистрация
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                client.connect()
                if(password_signUp.text.toString() == password_two.text.toString()) {
                    writeFile(nickname_signUp.text.toString(), password_signUp.text.toString(),true)
                    client.addUser(nickname_signUp.text.toString(),
                        name.text.toString(),password_signUp.text.toString(), email.text.toString())
                    }
                }
            }


        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun writeFile(nickname: String,password: String,isAuth: Boolean) {
        try {
            // отрываем поток для записи
            val bw = BufferedWriter(
                OutputStreamWriter(
                    openFileOutput("fileName.txt", Context.MODE_PRIVATE)
                )
            )
            // пишем данные
            bw.write("$nickname\n$password\n$isAuth")
            // закрываем поток
            bw.close()
            Log.d("Files: ", "Файл записан")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readFile(call : String):String {
        try {
            // открываем поток для чтения
            val br = BufferedReader(
                InputStreamReader(
                    openFileInput("fileName.txt")
                )
            )
            val nickname = br.readLine()
            val password = br.readLine()
            val isAuth = br.readLine()
            Log.d("1: ", nickname)
            Log.d("2: ", password)
            Log.d("3: ", isAuth)
            if (call == "nickname") return nickname
            if (call == "password") return password
            if (call == "isAuth") return isAuth
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
    fun startMain(){
        val intent = Intent(this, MainActivity::class.java)// вход
        startActivity(intent)
    }
}
