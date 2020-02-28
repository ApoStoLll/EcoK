package com.missclick.eco

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_log_in.*


class LogIn : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnLogIn).setOnClickListener {
            (activity as RegisterActivity).logIn()
        }
        view.findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            (activity as RegisterActivity).signUpMenu()
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener {
            (activity as RegisterActivity).startMain(3)
        }

    }


}
