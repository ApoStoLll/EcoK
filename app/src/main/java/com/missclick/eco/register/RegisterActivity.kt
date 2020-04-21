package com.missclick.eco.register

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import java.io.*


class RegisterActivity : AppCompatActivity(){

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

    fun writeAuthToFile(nickname: String,password: String) {
        try {
            val bw = BufferedWriter(
                OutputStreamWriter(
                    openFileOutput("AutoEntrance.txt", Context.MODE_PRIVATE)
                )
            )
            bw.write("$nickname\n$password\n")
            bw.close()
            Log.d("Files: ", "Файл записан")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun startMain(nickname: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nickname",nickname)
        startActivity(intent)
    }

    fun warnings(number : Int){
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
