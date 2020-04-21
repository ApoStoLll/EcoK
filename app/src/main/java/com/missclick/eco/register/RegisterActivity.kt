package com.missclick.eco.register

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import android.support.v4.app.ActivityCompat
import android.os.Build
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v4.content.ContextCompat
import android.Manifest.permission.READ_EXTERNAL_STORAGE





class RegisterActivity : AppCompatActivity(){

    private val client = HttpClient("95.158.11.238", 8080)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        val permission =
            arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityCompat.requestPermissions(this, permission, 1)
            }
        }
        */
        readFile()
        setContentView(R.layout.activity_register)
        logInMenu() // по дефолту запускаем ЛогИн меню
    }

    private fun logInMenu(){ // лоигИн меню интерфейс
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_holder, LogIn())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun signUpMenu(){ // сигнАп меню интерфейс
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(LogIn())
        transaction.replace(R.id.fragment_holder, SignUp())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun logIn(isAuth: Boolean,nickname :String = nickname_logIn.text.toString(),password: String = password_logIn.text.toString() ){
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try { // хз мб не работает
                    client.connect()
                    Log.d("Try","Success")
                }
                catch (e: NumberFormatException) {
                    Log.d("Try","Bad")
                    runOnUiThread {warnings(1)}
                }

                if (nickname.isNotEmpty() && password.isNotEmpty()){
                        if (client.checkUser(nickname,password)){
                            runOnUiThread {startMain(1,nickname)}
                        }
                        else runOnUiThread {warnings(2)}
                }
                else runOnUiThread {warnings(1)}
            }

        }
        if(isAuth)
            writeFile(nickname_logIn.text.toString(), password_logIn.text.toString())
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

                                    //writeFile(nickname_signUp.text.toString(), password_signUp.text.toString())
                                    if (client.addUser(
                                            nickname_signUp.text.toString(),
                                            name.text.toString(), password_signUp.text.toString(), email.text.toString()
                                        )
                                    ) {
                                        runOnUiThread {startMain(2)}
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

    private fun writeFile(nickname: String,password: String) {
        try {
            // отрываем поток для записи
            val bw = BufferedWriter(
                OutputStreamWriter(
                    openFileOutput("AutoEntrance.txt", Context.MODE_PRIVATE)
                )
            )
            // пишем данные
            bw.write("$nickname\n$password\n")
            // закрываем поток
            bw.close()
            Log.d("Files: ", "Файл записан")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFile(){
        try {
            // открываем поток для чтения
            val br = BufferedReader(
                InputStreamReader(
                    openFileInput("AutoEntrance.txt")
                )
            )
            val nickname = br.readLine()
            val password = br.readLine()
            logIn(false, nickname ,password)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun startMain(from: Int,nickname: String = nickname_logIn.text.toString()){
        val intent = Intent(this, MainActivity::class.java)// вход
        when(from){
            1-> intent.putExtra("nickname",nickname)
            2-> intent.putExtra("nickname",nickname_signUp.text.toString())
            else -> intent.putExtra("nickname","aloxa")
        }
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
