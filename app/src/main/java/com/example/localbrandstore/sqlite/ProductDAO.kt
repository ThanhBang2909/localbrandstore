package com.example.localbrandstore.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.localbrandstore.model.Products
import com.example.localbrandstore.model.ProductsModel

class ProductDAO(context: Context) {

    private val db: SQLiteDatabase

    init {
        val dbHelper = DBHelper(context)
        db = dbHelper.writableDatabase
    }

    fun getProducts(): ArrayList<Products> {
        val listProducts = ArrayList<Products>()
        val cursor = db.query("CART", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val sp = Products(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getString(3),
                cursor.getInt(4)
            )
            listProducts.add(sp)
        }
        cursor.close()
        return listProducts
    }

    fun insertProduct(id: String, name: String, image: String, price: Double, quantity: Int) {
        val cursor = db.query("CART", null, "productID=?", arrayOf(id), null, null, null)

        if (cursor.moveToFirst()) {
            val currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("productNumber"))
            val newQuantity = currentQuantity + quantity

            val values = ContentValues().apply {
                put("productNumber", newQuantity)
            }

            db.update("CART", values, "productID=?", arrayOf(id))

        } else {
            val values = ContentValues().apply {
                put("productID", id)
                put("productName", name)
                put("productPicture", image)
                put("productPrice", price)
                put("productNumber", quantity)
            }

            db.insert("CART", null, values)
        }

        cursor.close()
    }


    fun deleteProductById(id: String) {
        db.delete("CART", "productID=?", arrayOf(id))
        db.close()
    }

    fun updateProductQuantityById(id: String, quantity: Int) {
        val values = ContentValues()
        values.put("productNumber", quantity)
        db.update("CART", values, "productID=?", arrayOf(id))
    }

    fun reloadCart() {
        db.delete("CART", null, null)
        db.close()
    }

}