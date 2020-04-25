package com.missclick.eco.main.profile


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.missclick.eco.*
import com.missclick.eco.DBHelper
import com.missclick.eco.main.MainActivity
import kotlinx.coroutines.*
import java.io.*
import java.net.ConnectException
import android.content.ContentValues
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.transition.Hold
import kotlinx.android.synthetic.main.profile_positive_item.*


class Profile : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exitTransition = Hold()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFromFile()
        update()
        refresh.setOnRefreshListener{
            update()
            refresh.isRefreshing = false
        }
        view.findViewById<FloatingActionButton>(R.id.btnPlus ).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder,ProfilePlus())
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
                    (activity as MainActivity).runOnUiThread {updateToFile(user.name,user.score,user.imageName,actions)}
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
        if(name_profile == null) return
        val myAdapter = PositiveAdapter(
            actions,
            object : PositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {
                    val profileInfo = ProfilePostInfo()
                    val bundle = Bundle()
                    bundle.putParcelable("arg",item)
                    profileInfo.arguments = bundle
                    val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
                    transaction.addSharedElement(view!!, "info")
                    transaction.replace(R.id.fragment_holder, profileInfo)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            })
        //recV.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        recV.setHasFixedSize(true)
        recV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recV.adapter = myAdapter
        name_profile.text = user.name
        var image =  BitmapFactory.decodeFile(context!!.filesDir.path + "/" + user.imageName)
        image = if (image != null) Bitmap.createScaledBitmap(image, 250, 250, false) else return
        image_profile.setImageBitmap(image)
        score_profile.text = user.score
    }

}