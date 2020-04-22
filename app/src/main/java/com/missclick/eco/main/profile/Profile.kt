package com.missclick.eco.main.profile


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.missclick.eco.HttpClient
import com.missclick.eco.ProfilePositive
import com.missclick.eco.R
import com.missclick.eco.User
import com.missclick.eco.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import java.io.*
import java.net.ConnectException

class Profile : Fragment() {

    private var items:List<PositiveItem> = listOf()
    val profile = ProfilePositive()
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFromFile()
        update()
        swipeRefreshLayout.setOnRefreshListener{
            update()
            swipeRefreshLayout.isRefreshing = false
        }
        view.findViewById<Button>(R.id.btnAddPos_profile ).setOnClickListener {
            bundle.putBoolean("arg",true)
            profile.arguments = bundle
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder,profile)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        view.findViewById<Button>(R.id.btnAddNeg_profile ).setOnClickListener {
            bundle.putBoolean("arg",false)
            profile.arguments = bundle
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder, profile)
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

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun update(){
        Log.e("UPD", "UPDATE")
        fun upd(user : User){
            updateToFile(user.name,user.score,user.imageName)
            if(name_profile == null) return
            val myAdapter = PositiveAdapter(
                items,
                object : PositiveAdapter.Callback {
                    override fun onItemClicked(item: PositiveItem) {

                    }
                })
            recV.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL,false)
            recV.adapter = myAdapter
            name_profile.text = user.name
            var image = BitmapFactory.decodeFile(context!!.filesDir.path + "/" + user.imageName)
            if (image == null) return
            image = Bitmap.createScaledBitmap(image, 400, 400, false)
            image_profile.setImageBitmap(image)
            score_profile.text = user.score
        }

        GlobalScope.launch {

            val client = HttpClient("95.158.11.238", 8080)//(activity as MainActivity).client
            withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    val user = client.getUserData((activity as MainActivity).nickname, (activity as MainActivity))
                    client.connect()
                    items = client.getProfilePost((activity as MainActivity).nickname)
                    (activity as MainActivity).runOnUiThread { upd(user) }
                }catch (e : ConnectException){
                    Log.e("ERROR", e.toString())
                }
            }
        }

    }
    private fun updateToFile(name: String,score: String,imageName: String){
        //todo upd to db
        try {
            // отрываем поток для записи
            val bw = BufferedWriter(
                OutputStreamWriter(
                    (activity as MainActivity).openFileOutput((activity as MainActivity).nickname+".txt", Context.MODE_PRIVATE)
                )
            )
            bw.write("$name\n$score\n$imageName")
            // закрываем поток
            bw.close()

        } catch (e: FileNotFoundException){
            return
        }
    }
    private fun updateFromFile(){
        //todo upd from db
        try {
            // открываем поток для чтения
            val br = BufferedReader(
                InputStreamReader(
                    (activity as MainActivity).openFileInput((activity as MainActivity).nickname+".txt")
                )
            )
            name_profile.text = br.readLine()
            score_profile.text = br.readLine()
            val imageName = br.readLine()
            var image = BitmapFactory.decodeFile(context!!.filesDir.path + "/" + imageName)
            if (image == null) return
            image = Bitmap.createScaledBitmap(image, 400, 400, false)
            image_profile.setImageBitmap(image)
        }catch (e: FileNotFoundException) {
            return
        }
    }

}