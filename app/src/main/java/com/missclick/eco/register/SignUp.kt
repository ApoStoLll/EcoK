package com.missclick.eco.register

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUp : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            signUp(checkBoxSignUp.isChecked,nicknameSignUp.text.toString(), nameSignUp.text.toString(),
                passwordSignUp.text.toString(),passwordRepeatSignUp.text.toString(), emailSignUp.text.toString())
        }
    }

    private fun signUp(isAuth: Boolean, nickname : String, name : String , password : String, passwordRepeat : String,
                       email : String){

        val activity = activity as RegisterActivity
        val client = HttpClient("95.158.11.238", 8080)

        if (nickname.length < 3){
            activity.warnings(3)
            return
        }
        if (name.isEmpty()){
            activity.warnings(4)
            return
        }
        if (password.length < 6){
            activity.warnings(5)
            return
        }
        if(password != passwordRepeat){
            activity.warnings(7)
            return
        }
        if(email.isEmpty()){//todo проверка почты
            activity.warnings(6)
            return
        }
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                client.connect()
                if (client.addUser(nickname,name,password,email))
                {
                    if(isAuth) activity.writeAuthToFile(nickname,password)
                    activity.runOnUiThread {activity.startMain(nickname)}
                } else activity.runOnUiThread {activity.warnings(8)}
            }
        }
    }

}
