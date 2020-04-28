package com.missclick.eco.register

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException


class LogIn : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        readAuthFromFile()
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnLogIn).setOnClickListener {
            var isAuth = 0
            if(checkBoxLogIn.isChecked) isAuth = 1
            logIn(isAuth,nicknameLogIn.text.toString(),passwordLogIn.text.toString())
        }
        view.findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            (activity as RegisterActivity).signUpMenu()
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener {  //временно
            (activity as RegisterActivity).startMain("aloxa")
        }
    }

    private fun logIn(isAuth : Int, nickname : String, password : String){

        val activity = activity as RegisterActivity
        val client = HttpClient("95.158.11.238", 8080)

        if (nickname.isEmpty() || password.isEmpty()) {
            activity.warnings(1)
            return
        }
        GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try { // хз мб не работает
                        client.connect()
                        Log.d("Try", "Success")
                        if (client.checkUser(nickname, password)) {
                            activity.runOnUiThread {
                                if(isAuth == 1)
                                    activity.writeAuthToFile(nickname, password)
                                activity.runOnUiThread{activity.startMain(nickname)}
                            }
                        } else activity.runOnUiThread { activity.warnings(2) }
                    }catch (e : ConnectException){
                        activity.runOnUiThread { activity.warnings(9) }
                        Log.e("ERROR", e.toString())
                        if(isAuth == 3) activity.runOnUiThread{activity.startMain(nickname)}
                    }
                }
        }
    }

    private fun readAuthFromFile(){
        try {
            val br = BufferedReader(
                InputStreamReader(
                    (activity as RegisterActivity).openFileInput("AutoEntrance.txt")
                )
            )
            val nickname = br.readLine()
            val password = br.readLine()
            logIn(3, nickname, password)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
