package com.example.localbrandstore.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.localbrandstore.R
import com.example.localbrandstore.adapter.CartAdapter
import com.example.localbrandstore.adapter.CheckOutAdapter
import com.example.localbrandstore.model.OrderRequest
import com.example.localbrandstore.model.Products
import com.example.localbrandstore.model.ResponseModel
import com.example.localbrandstore.model.UserModel
import com.example.localbrandstore.service.ApiService
import com.example.localbrandstore.service.RetrofitClient
import com.example.localbrandstore.sqlite.ProductDAO
import com.example.localbrandstore.util.AndroidToast
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity() {

    private lateinit var rvDataCart: RecyclerView
    private lateinit var productDAO: ProductDAO
    private var mListProduct: ArrayList<Products> = ArrayList()
    private lateinit var checkOutAdapter: CheckOutAdapter
    private lateinit var btnBack: ImageView
    private var cartTotal: Double = 0.0
    private lateinit var tvCartTotal: TextView
    private lateinit var namePay: TextView
    private lateinit var phonePay: TextView
    private lateinit var addressPay: TextView
    private lateinit var layoutAddressDelivery: LinearLayout
    private lateinit var btnCheckout: Button
    private var userEmail: String = ""
    private var userFullName: String = ""
    private var userPassword: String = ""
    private var userAddress: String = ""
    private var userPhone: String = ""
    private val androidToast: AndroidToast?= AndroidToast()
    private val apiService = RetrofitClient.getApiService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initView()
        readUserDataLocal()
        getAddress()
        getProduct()
        eventClick()
    }

    private fun eventClick(){
        btnCheckout.setOnClickListener{
            callApiCheckout()
        }
    }


    private fun getProduct(){
        productDAO = ProductDAO(this)

        mListProduct = productDAO.getProducts()
        checkOutAdapter = CheckOutAdapter(this, mListProduct)
        rvDataCart.adapter = checkOutAdapter
        rvDataCart.layoutManager = LinearLayoutManager(this)
        rvDataCart.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }


    private fun getAddress(){
        namePay.text = userFullName
        phonePay.text = userPhone
        addressPay.text = userAddress
    }


    private fun readUserDataLocal(){
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        userEmail = sharedPreferences.getString("user_email", null).toString()
        userFullName = sharedPreferences.getString("user_fullName", null).toString()
        userPassword = sharedPreferences.getString("user_password", null).toString()
        userAddress = sharedPreferences.getString("user_address", null).toString()
        userPhone = sharedPreferences.getString("user_phone", null).toString()

    }


    private fun callApiCheckout() {
        val name = namePay.text.toString().trim()
        val address = addressPay.text.toString().trim()
        val phone = phonePay.text.toString().trim()

        if (name.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty()) {
            val requestQueue = Volley.newRequestQueue(applicationContext)
            val stringRequest = object : StringRequest(
                Method.POST,
                ApiService.orderPath,
                Response.Listener { madonhang ->
                    if (Integer.parseInt(madonhang) > 0) {
                        val queue = Volley.newRequestQueue(applicationContext)
                        val request = object : StringRequest(
                            Method.POST,
                            ApiService.orderDetailPath,
                            Response.Listener { response ->
                                if (response == "success") {
                                    mListProduct.clear()
                                    productDAO.reloadCart()
                                    androidToast?.showToast(applicationContext, "Đặt hàng thành công")
                                } else {
                                    androidToast?.showToast(applicationContext, "Dữ liệu lỗi")
                                }
                            },
                            Response.ErrorListener { error ->

                            }) {
                            override fun getParams(): MutableMap<String, String> {
                                val jsonArray = JSONArray()
                                for (i in mListProduct.indices) {
                                    val jsonObject = JSONObject()
                                    try {
                                        jsonObject.put("orderID", madonhang)
                                        jsonObject.put("productID", mListProduct[i].masp)
                                        jsonObject.put("productName", mListProduct[i].tensp)
                                        jsonObject.put("productPrice", mListProduct[i].giasp)
                                        jsonObject.put("productPicture", mListProduct[i].hinhsp)
                                        jsonObject.put("productNumber", mListProduct[i].soLuong)
                                        jsonObject.put("total", mListProduct[i].giasp * mListProduct[i].soLuong)
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    jsonArray.put(jsonObject)
                                }
                                val hashMap = HashMap<String, String>()
                                hashMap["json"] = jsonArray.toString()
                                return hashMap
                            }
                        }
                        queue.add(request)
                    }
                },
                Response.ErrorListener { error ->

                }) {
                override fun getParams(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap["customerName"] = name
                    hashMap["phoneNumber"] = phone
                    hashMap["customerAddress"] = address
                    hashMap["customerTotalPrice"] = cartTotal.toString()
                    hashMap["customerStatus"] = "0"
                    hashMap["email"] = userEmail
                    return hashMap
                }
            }
            requestQueue.add(stringRequest)
        } else {
            androidToast?.showToast(applicationContext,"Kiểm tra lại dữ liệu")
        }
    }


    private fun initView(){
        rvDataCart = findViewById(R.id.rvCart)
        btnBack = findViewById(R.id.btnBack)
        tvCartTotal = findViewById(R.id.moneyPay)
        namePay = findViewById(R.id.namePay)
        phonePay = findViewById(R.id.phonePay)
        addressPay = findViewById(R.id.addressPay)
        btnCheckout = findViewById(R.id.btnCheckOut)
        cartTotal = intent.getDoubleExtra("cartTotal",0.0)
        tvCartTotal.text = cartTotal.toString()

    }
}