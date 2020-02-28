package com.missclick.eco

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*


class RegisterActivity : AppCompatActivity(){

    private val client = HttpClient("95.158.11.238", 8080)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // create upload of file
        //if(readFile("isAuth")=="true") startMain()
        //startMain() // could be changed
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
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try { // хз мб не работает
                    client.connect()
                }
                catch (e: NumberFormatException) {
                    runOnUiThread {warnings(1)}
                }

                if (nickname_logIn.text.toString().isNotEmpty() && password_logIn.text.toString().isNotEmpty()){
                        if (client.checkUser(nickname_logIn.text.toString(), password_logIn.text.toString())){
                            runOnUiThread {startMain()}
                        }
                        else runOnUiThread {warnings(2)}
                }
                else runOnUiThread {warnings(1)}
            }

        }
        writeFile(nickname_logIn.text.toString(), password_logIn.text.toString(),true)
    }

    fun signUp(){ // регистрация
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                client.connect()
                if (nickname_signUp.text.toString().length>2){
                    if (nickname_signUp.text.toString().isNotEmpty()){
                        if (password_signUp.text.toString().length>5){
                            if (true){ // сделать проверку на почту
                                if (password_signUp.text.toString() == password_two.text.toString()) {

                                    writeFile(nickname_signUp.text.toString(), password_signUp.text.toString(), true)
                                    if (client.addUser(
                                            nickname_signUp.text.toString(),
                                            name.text.toString(), password_signUp.text.toString(), email.text.toString()
                                        )
                                    ) {
                                        runOnUiThread {startMain()}
                                    } else runOnUiThread {warnings(8)}

                                } else runOnUiThread {warnings(7)}
                            }
                            else runOnUiThread {warnings(6)}
                        }
                        else runOnUiThread {warnings(5)}
                    }
                    else runOnUiThread {warnings(4)}
                }
                else runOnUiThread {warnings(3)}
            }
        }

    }

    private fun writeFile(nickname: String,password: String,isAuth: Boolean) {
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

    private fun readFile(call : String):String {
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
    private fun startMain(){
        val intent = Intent(this, MainActivity::class.java)// вход
        startActivity(intent)
    }
    private fun warnings(number : Int){
        when(number){
            1-> Toast.makeText(this,getString(R.string.fieldsIsEmpty),Toast.LENGTH_SHORT).show()
            2-> Toast.makeText(this,getString(R.string.wrongData),Toast.LENGTH_SHORT).show()
            3-> Toast.makeText(this,getString(R.string.shortNickname),Toast.LENGTH_SHORT).show()
            4-> Toast.makeText(this,getString(R.string.nameIsEmpty),Toast.LENGTH_SHORT).show()
            5-> Toast.makeText(this,getString(R.string.shortPassword),Toast.LENGTH_SHORT).show()
            6-> Toast.makeText(this,getString(R.string.emailIsBad),Toast.LENGTH_SHORT).show()
            7-> Toast.makeText(this,getString(R.string.passwordDNMatch),Toast.LENGTH_SHORT).show()
            8-> Toast.makeText(this,getString(R.string.nicknameIsBusy),Toast.LENGTH_SHORT).show()
        }
    }
}
