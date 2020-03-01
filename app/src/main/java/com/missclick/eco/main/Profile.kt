package com.missclick.eco.main


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.HttpClient
import com.missclick.eco.Profile_positive
import com.missclick.eco.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Profile : Fragment() {

    private val client = HttpClient("95.158.11.238", 8080)

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update()
        view.findViewById<Button>(R.id.btnAddPos_profile ).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder, Profile_positive())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun update(){
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                client.connect()
                val user = client.getUserData((activity as MainActivity).nickname)
                //name_profile = user.get
            }
        }
    }

    fun addPositive (){

    }

    fun addNegative(){

    }

    fun settings(){

    }
}