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
import com.missclick.eco.*
import com.missclick.eco.DBHelper
import com.missclick.eco.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import java.io.*
import java.net.ConnectException
import android.content.ContentValues



class Profile : Fragment() {

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

    private fun update(){
        GlobalScope.launch {
            val client = HttpClient("95.158.11.238", 8080)//(activity as MainActivity).client
            withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    val user = client.getUserData((activity as MainActivity).nickname, (activity as MainActivity))
                    client.connect()
                    val actions = client.getProfilePost((activity as MainActivity).nickname)
                    (activity as MainActivity).runOnUiThread { upd(user,actions) }
                }catch (e : ConnectException){
                    Log.e("ERROR", e.toString())
                }
            }
        }
    }

    private fun updateToFile(name: String,score: String,imageName: String,actions: List<PositiveItem>){
        val activity = activity as MainActivity
        val dbHelper = DBHelper(activity)
        val db = dbHelper.getWritableDatabase()
        val data = ContentValues()
        db.delete("posts", null, null)
        for (item in actions){
            data.put("itemId",item.id)
            data.put("name",item.action)
            data.put("score",item.score)
            data.put("time",item.time)
            db.insert("posts", null, data)
        }

        try {
            val bw = BufferedWriter(
                OutputStreamWriter(
                    activity.openFileOutput(activity.nickname+".txt", Context.MODE_PRIVATE)
                )
            )
            bw.write("$name\n$score\n$imageName")
            bw.close()
        } catch (e: FileNotFoundException){
            return
        }
    }

    private fun updateFromFile(){
        val dbHelper = DBHelper(activity as MainActivity)
        val db = dbHelper.getWritableDatabase()
        val actions:MutableList<PositiveItem> = mutableListOf()
        val c = db.query("posts", null, null, null, null, null, null)
        if (c.moveToFirst())
        {
            do{
                val idCol = c.getString(c.getColumnIndex("itemId")).toInt()
                val nameCol = c.getString(c.getColumnIndex("name"))
                val scoreCol =  c.getString(c.getColumnIndex("score")).toInt()
                val timeCol = c.getString(c.getColumnIndex("time"))
                    actions.add(PositiveItem(idCol,nameCol,scoreCol,timeCol))
                }
            while (c.moveToNext())
        }
        else c.close()

        try {
            // открываем поток для чтения
            val br = BufferedReader(
                InputStreamReader(
                    (activity as MainActivity).openFileInput((activity as MainActivity).nickname+".txt")
                )
            )
            val name = br.readLine()
            val score = br.readLine()
            val imageName = br.readLine()
            upd(User("null",name,score,imageName),actions)
        }catch (e: FileNotFoundException) {
            return
        }
    }

    private fun upd(user : User,actions:List<PositiveItem>){
        updateToFile(user.name,user.score,user.imageName,actions)
        if(name_profile == null) return
        val myAdapter = PositiveAdapter(
            actions,
            object : PositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {

                }
            })
        recV.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL,false)
        recV.adapter = myAdapter
        name_profile.text = user.name
        var image =  BitmapFactory.decodeFile(context!!.filesDir.path + "/" + user.imageName)
        image = if (image != null) Bitmap.createScaledBitmap(image, 400, 400, false) else return
        image_profile.setImageBitmap(image)
        score_profile.text = user.score
    }

}