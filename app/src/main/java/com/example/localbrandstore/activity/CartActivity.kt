package com.example.localbrandstore.activity

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.R
import com.example.localbrandstore.adapter.CartAdapter
import com.example.localbrandstore.model.Products
import com.example.localbrandstore.sqlite.DBHelper
import com.example.localbrandstore.sqlite.ProductDAO
import com.example.localbrandstore.util.AndroidToast

class CartActivity : AppCompatActivity() {

    lateinit var cartTotal: TextView
    private lateinit var checkout: Button
    private lateinit var rvCart: RecyclerView
    private lateinit var context: Context
    private var mListProduct: ArrayList<Products> = ArrayList()
    private lateinit var productDAO: ProductDAO
    private lateinit var btnBack: ImageView
    private var totalPrice: Double = 0.0
    private lateinit var db: SQLiteDatabase
    private lateinit var cartAdapter: CartAdapter


    private var iD: String = ""
    private var name: String = ""
    private var img: String = ""
    private var quality: Int = 0
    private var price: Double = 0.0

    private val androidToast: AndroidToast = AndroidToast()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initView()
        eventClick()
        addProduct()
        getTotalPrice()
    }

    private fun eventClick(){
        btnBack.setOnClickListener{
            finish()
        }

        checkout.setOnClickListener{
            if (productDAO.getProducts().size>=1){
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("cartTotal", cartTotal.text.toString().trim())
                startActivity(intent)
            }else{
                androidToast.showToast(this, "Gio hàng trống")
            }
        }

    }

    private fun addProduct(){
        val bundle = intent.extras
        if (bundle != null && !bundle.isEmpty){
            iD = bundle.getString("id") ?: ""
            name = bundle.getString("name") ?: ""
            img = bundle.getString("image").toString()
            price = bundle.getDouble("price", 1.0)
            quality = bundle.getInt("quantity", 1)
            productDAO.insertProduct(iD, name, img, price, quality)
            cartAdapter.notifyDataSetChanged()

        }else{
            mListProduct = productDAO.getProducts()
        }
    }

    private fun getTotalPrice() {
        totalPrice = 0.0
        for (sp in mListProduct) {
            totalPrice += sp.giasp * sp.soLuong
        }
        cartTotal.text = "$totalPrice Đ"
    }

    fun updateTotal(total: Double) {
        cartTotal.text = "$total Đ"
    }


    private fun initView(){
        cartTotal = findViewById(R.id.cart_total)
        checkout = findViewById(R.id.btnCheckout)
        rvCart = findViewById(R.id.rvCart)
        btnBack = findViewById(R.id.btnBack)

        cartTotal.text = "$totalPrice Đ"

        productDAO = ProductDAO(this)

        mListProduct = productDAO.getProducts()
        cartAdapter = CartAdapter(this, mListProduct)
        rvCart.adapter = cartAdapter
        rvCart.layoutManager = LinearLayoutManager(this)
        rvCart.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        try {
            val dbHelper = DBHelper(this)
            db = dbHelper.writableDatabase
        } catch (e: Exception) {
            Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show()
        }

    }

}