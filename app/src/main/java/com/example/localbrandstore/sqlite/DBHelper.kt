package com.example.localbrandstore.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, CSDL, null, Version) {

    companion object{
        const val CSDL = "QLProduct03"
        const val Version = 1
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        val sql = "CREATE TABLE CART (productID TEXT PRIMARY KEY, productName TEXT,productPrice DOUBLE, productPicture TEXT,  productNumber INTEGER);"
        sqLiteDatabase?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}