package com.missclick.eco.main.profile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.missclick.eco.HttpClient

import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile_positive.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException


class ProfileEditPost : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_edit_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments!!.getParcelable<PositiveItem>("arg")
        view.findViewById<Button>(R.id.btnPost ).setOnClickListener {
            requestToServer(item)
            (activity as MainActivity).startMenu(3)
        }

    }

    private fun requestToServer(item : PositiveItem){
        GlobalScope.launch {
            val client = HttpClient("95.158.11.238", 8080)
            withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    client.addProfilePost(item,(activity as MainActivity).nickname)
                }catch (e : ConnectException){
                    Log.e("ERROR", e.toString())
                }

            }
        }
    }

}
