package com.example.localbrandstore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.localbrandstore.R
import com.example.localbrandstore.service.ApiService
import com.example.localbrandstore.util.AndroidToast
import com.squareup.picasso.Picasso

class DetailProductActivity : AppCompatActivity() {

    private lateinit var imgProductDetail: ImageView
    private lateinit var btnBack: ImageView
    private lateinit var tvNameProduct: TextView
    private lateinit var tvPriceProduct: TextView
    private lateinit var tvDesProduct: TextView
    private lateinit var btnAddToCart: Button
    private var productID: String = ""
    private var productName: String = ""
    private var productImg: String = ""
    private var productDes: String = ""
    private var productPrice: Double = 0.0
    private val androidToast: AndroidToast = AndroidToast()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        initView()
        eventClick()
        loadProductDetail()
    }

    private fun eventClick(){
        btnBack.setOnClickListener{
            finish()
        }

        btnAddToCart.setOnClickListener{
            val intent = Intent(this, CartActivity::class.java)
            val bundle = Bundle().apply {
                putString("id", productID)
                putString("name", productName)
                putString("image", productImg)
                putDouble("price", productPrice)
                putInt("quantity", 1)
            }
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }

    }

    private fun loadProductDetail() {
        val bundle = intent.extras
        if (bundle != null) {
            if (!bundle.isEmpty) {
                productID = bundle.getString("maSanPham", "")
                productName = bundle.getString("name", "")
                productDes = bundle.getString("mota", "")
                productImg = bundle.getString("hinh", "")
                productPrice = bundle.getDouble("gia", 0.0)

                tvNameProduct.text = productName
                tvDesProduct.text = productDes
                tvPriceProduct.text = productPrice.toString()
                Picasso.get().load(ApiService.imgProductPath + productImg).into(imgProductDetail)
            } else {
                androidToast.showToast(applicationContext, "Không có data")
            }
        }
    }


    private fun initView(){
        tvNameProduct = findViewById(R.id.tvNameProductsDetail)
        tvPriceProduct = findViewById(R.id.tvPriceProductDetail)
        imgProductDetail = findViewById(R.id.imgProductDetail)
        tvDesProduct = findViewById(R.id.tvDetail)
        btnBack = findViewById(R.id.btnDetailBack)
        btnAddToCart = findViewById(R.id.btnAddCard)
    }

}