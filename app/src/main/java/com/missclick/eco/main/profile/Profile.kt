package com.missclick.eco.main.profile


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.DBHelper
import com.missclick.eco.main.MainActivity
import kotlinx.coroutines.*
import java.io.*
import java.net.ConnectException
import android.content.ContentValues
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import com.missclick.eco.User



class Profile : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exitTransition = MaterialFadeThrough.create(requireContext())
        reenterTransition = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Z, true)
        (activity as MainActivity).setSupportActionBar(profile_toolbar)
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

        view.findViewById<Button>(R.id.btnEditProfile ).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder, EditProfile())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        view.findViewById<Button>(R.id.settings_btn ).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder, Settings())
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    private fun update(){ // Костыль с трай кэч
        GlobalScope.launch ( Dispatchers.IO ) {
            withContext(Dispatchers.Main){
                loadingPanelProfile.visibility = View.VISIBLE
            }
            lateinit var user : User
            lateinit var actions : List<PositiveItem>
            val client = HttpClient("95.158.11.238", 8080)
            try {
                client.connect()
                user = client.getUserData((activity as MainActivity).nickname, (activity as MainActivity))
            } catch (e : ConnectException){
                Log.e("ERROR", e.toString())
                withContext(Dispatchers.Main) { loadingPanelProfile.visibility = View.GONE }
                cancel()
            }
            try{
                client.connect()
                actions = client.getProfilePost((activity as MainActivity).nickname)
            } catch (e : ConnectException){
                Log.e("ERROR", e.toString())
                withContext(Dispatchers.Main) { loadingPanelProfile.visibility = View.GONE }
                cancel()
            }
            withContext(Dispatchers.Main){
                loadingPanelProfile.visibility = View.GONE
                upd(user, actions)
                updateToFile(user.name,user.score,user.imageName,actions)
            }
        }
//        GlobalScope.launch(Dispatchers.Main) {
//            loadingPanelProfile.visibility = View.VISIBLE
//            val client = HttpClient("95.158.11.238", 8080)
//            val user = withContext(Dispatchers.IO){
//                try {
//                    client.connect()
//                    client.getUserData((activity as MainActivity).nickname, (activity as MainActivity))
//                } catch (e : ConnectException){
//                    Log.e("ERROR", e.toString())
//
//                }
//            }
//
//            val actions = withContext(Dispatchers.IO){
//                try {
//                    client.connect()
//                } catch (e : ConnectException){
//                    Log.e("ERROR", e.toString())
//                }
//                client.getProfilePost((activity as MainActivity).nickname)
//            }
//            loadingPanelProfile.visibility = View.GONE
//            upd(user,actions)
//            updateToFile(user.name,user.score,user.imageName,actions)
//        }
    }

    private fun updateToFile(name: String,score: String,imageName: String,actions: List<PositiveItem>){
        val activity = activity as MainActivity
        val dbHelper = DBHelper(activity)
        val db = dbHelper.writableDatabase
        val data = ContentValues()
        db.delete("posts", null, null)
        for (item in actions){
            data.put("itemId",item.id)
            data.put("name",item.action)
            data.put("score",item.score)
            data.put("time",item.time)
            data.put("share",item.share.toString())
            data.put("description",item.description)
            data.put("imageName",item.imageName)
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
        val db = dbHelper.writableDatabase
        val actions:MutableList<PositiveItem> = mutableListOf()
        val c = db.query("posts", null, null, null, null, null, null)
        if (c.moveToFirst())
        {
            do{
                val idCol = c.getString(c.getColumnIndex("itemId")).toInt()
                val nameCol = c.getString(c.getColumnIndex("name"))
                val scoreCol =  c.getString(c.getColumnIndex("score")).toInt()
                val timeCol = c.getString(c.getColumnIndex("time"))
                val shareCol = c.getString(c.getColumnIndex("share")).toBoolean()
                val descriptionCol = c.getString(c.getColumnIndex("description"))
                val imageNameCol = c.getString(c.getColumnIndex("imageName"))
                    actions.add(PositiveItem(idCol,nameCol,scoreCol,timeCol,descriptionCol,shareCol,imageNameCol))
                }
            while (c.moveToNext())
        }
        else c.close()

        try {
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

    private fun upd(user: User, actions:List<PositiveItem>){
        if(name_profile == null) return

        name_profile.text = user.name
        score_profile.text = user.score
        profile_name_toolbar.text = (activity as MainActivity).nickname
        var image =  BitmapFactory.decodeFile(context?.filesDir?.path + "/" + user.imageName)
        image = if (image != null) Bitmap.createScaledBitmap(image, 250, 250, false) else return
        image_profile.setImageBitmap(image)

        val myAdapter = PositiveAdapter(
            actions,
            object : PositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {
                    val profileInfo = ProfilePostInfo()
                    val bundle = Bundle()
                    bundle.putParcelable("arg",item)
                    profileInfo.arguments = bundle
                    val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
                   // transaction.addSharedElement(view!!, "info")
                    transaction.replace(R.id.fragment_holder, profileInfo)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            })
        recV.setHasFixedSize(true)
        recV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recV.adapter = myAdapter
    }

}