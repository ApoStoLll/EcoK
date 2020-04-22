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

    private fun update(){

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
        val activity = activity as MainActivity
        /*
        val db = activity.getBaseContext().openOrCreateDatabase(activity.nickname + ".db", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE posts (actions TEXT, score INTEGER,time TEXT)")

        db.execSQL("INSERT INTO posts ($act, $score, $time) VALUES (actions, score, time)")
        */
        val act = "action123"
        val score1 = items[0].score
        val time = items[0].time
        val dbHelper = DBHelper(activity)
        val db = dbHelper.getWritableDatabase()
        val cv = ContentValues()
        cv.put("name",act)
        cv.put("score",score1)
//        cv.put("time",time)
        db.insert("posts", null, cv)

         val c = db.query("posts", null, null, null, null, null, null)

      // ставим позицию курсора на первую строку выборки
      // если в выборке нет строк, вернется false
      if (c.moveToFirst())
{

 // определяем номера столбцов по имени в выборке
        val idColIndex = c.getColumnIndex("id")
    val nameColIndex = c.getColumnIndex("name")
    val emailColIndex = c.getColumnIndex("score")
    //val emailColIndex1 = c.getColumnIndex("time")

do
{
 // получаем значения по номерам столбцов и пишем все в лог
          Log.e("LOG_TAG",
              "ID = " + c.getInt(idColIndex) +
              ", name = " + c.getString(nameColIndex) +
              ", score = " + c.getString(emailColIndex)
          )
 // переход на следующую строку
          // а если следующей нет (текущая - последняя), то false - выходим из цикла
        }
while (c.moveToNext())
}
else
Log.e("feseges", "0 rows")
      c.close()


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
            image = if (image != null) Bitmap.createScaledBitmap(image, 400, 400, false) else return
            image_profile.setImageBitmap(image)
        }catch (e: FileNotFoundException) {
            return
        }
    }

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
        var image =  BitmapFactory.decodeFile(context!!.filesDir.path + "/" + user.imageName)
        image = if (image != null) Bitmap.createScaledBitmap(image, 400, 400, false) else return
        image_profile.setImageBitmap(image)
        score_profile.text = user.score
    }

}