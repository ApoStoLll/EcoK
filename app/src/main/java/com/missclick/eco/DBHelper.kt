package com.missclick.eco

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


internal class DBHelper(context: Context)// конструктор суперкласса
:   SQLiteOpenHelper(context, "myDB.db", null, 1) {

    override fun onCreate(db:SQLiteDatabase) {
    Log.d("LOG_TAG", "--- onCreate database ---")
     // создаем таблицу с полями
          db.execSQL("create table posts (" + "id integer primary key autoincrement," + "name text," + "score integer"  +");")
    }

    override fun onUpgrade(db:SQLiteDatabase, oldVersion:Int, newVersion:Int) {

    }
}