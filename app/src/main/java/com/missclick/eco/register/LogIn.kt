package com.missclick.eco.register

import android.os.Bundle
import android.support.v4.app.Fragment
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


class LogIn : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readAuthFromFile()
        view.findViewById<Button>(R.id.btnLogIn).setOnClickListener {
            logIn(checkBoxLogIn.isChecked,nicknameLogIn.text.toString(),passwordLogIn.text.toString())
        }
        view.findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            (activity as RegisterActivity).signUpMenu()
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener {  //временно
            (activity as RegisterActivity).startMain("aloxa")
        }
    }

    private fun logIn(isAuth : Boolean, nickname : String, password : String){

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
                                if(isAuth)
                                    activity.writeAuthToFile(nickname, password)
                                activity.runOnUiThread{activity.startMain(nickname)}
                            }
                        } else activity.runOnUiThread { activity.warnings(2) }

                    } catch (e: NumberFormatException) {
                        Log.d("Try", "Bad")
                        activity.runOnUiThread { activity.warnings(1) }
                    }catch (e : ConnectException){
                        Log.e("ERROR", e.toString())
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
            logIn(false, nickname, password)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
