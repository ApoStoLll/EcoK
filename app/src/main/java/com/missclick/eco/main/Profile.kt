package com.missclick.eco.main


import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.HttpClient
import com.missclick.eco.ProfilePositive
import com.missclick.eco.R
import kotlinx.android.synthetic.main.fragment_profile.*
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
            transaction.replace(R.id.fragment_holder, ProfilePositive())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        view.findViewById<Button>(R.id.btnSettings ).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder, Settings())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun update(){
        fun setImg(image: Bitmap){
            image_profile.setImageBitmap(image)
        }
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                client.connect()
                val user = client.getUserData((activity as MainActivity).nickname,(activity as MainActivity))
                name_profile.text = user.name
                (activity as MainActivity).runOnUiThread{setImg(user.image)}
                score_profile.text = user.score
            }
        }

    }

    fun addPositive (){

    }

    fun addNegative(){

    }
}