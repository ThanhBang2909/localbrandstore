package com.example.localbrandstore.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.localbrandstore.R
import com.example.localbrandstore.adapter.ProductAdapter
import com.example.localbrandstore.adapter.ProductByCategoryAdapter
import com.example.localbrandstore.model.ProductsModel
import com.example.localbrandstore.service.RetrofitClient
import com.example.localbrandstore.util.AndroidToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductByCategoryActivity : AppCompatActivity() {

    private lateinit var productByCategoryAdapter: ProductByCategoryAdapter
    private lateinit var rvProductByCategory: RecyclerView
    private lateinit var btnBack: ImageView
    private var catID: String = ""
    private lateinit var edtSearch: EditText
    private val apiService = RetrofitClient.getApiService()
    private val androidToast: AndroidToast = AndroidToast()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_by_category)
        initVew()
        eventClick()
        loadListProduct(catID)
        searchProduct()
    }

    private fun eventClick(){
        btnBack.setOnClickListener{
            finish()
        }
    }


    private fun loadListProduct(catID: String){
        apiService.getListProductsByCategory(catID).enqueue(object : Callback<List<ProductsModel>>{
            override fun onResponse(
                call: Call<List<ProductsModel>>,
                response: Response<List<ProductsModel>>
            ) {
                if (response.isSuccessful){
                    val products = response.body()
                    if (products != null){
                        productByCategoryAdapter = ProductByCategoryAdapter(applicationContext, products)
                        productByCategoryAdapter.notifyDataSetChanged()
                        rvProductByCategory.adapter = productByCategoryAdapter
                    }
                }else{
                    androidToast.showToast(applicationContext, "Lỗi")
                }
            }

            override fun onFailure(call: Call<List<ProductsModel>>, t: Throwable) {
                androidToast.showToast(applicationContext, "Call API ERR")
            }

        })
    }

    private fun searchProduct(){
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val chuoitim = charSequence.toString()
                productByCategoryAdapter.filter.filter(chuoitim)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }


    private fun initVew(){
        rvProductByCategory = findViewById(R.id.rvProductByCategory)
        btnBack = findViewById(R.id.btnBack)
        edtSearch = findViewById(R.id.searchProductByCategory)

        rvProductByCategory.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        val intent: Intent = getIntent()
        catID = intent.getStringExtra("catID") ?: "defaultValue"

    }
}