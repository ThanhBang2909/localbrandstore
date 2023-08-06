package com.example.localbrandstore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.localbrandstore.MainActivity
import com.example.localbrandstore.R
import com.example.localbrandstore.model.ResponseModel
import com.example.localbrandstore.model.UserModel
import com.example.localbrandstore.service.ApiService
import com.example.localbrandstore.service.RetrofitClient
import com.example.localbrandstore.util.AndroidToast
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class RegisterActivity : AppCompatActivity() {

    private var emailReg: EditText ?= null
    private var passwordReg: EditText ?= null
    private var fullNameReg: EditText ?= null
    private var addressReg: EditText ?= null
    private var phoneReg: EditText ?= null
    private var btnRegister: Button ?= null
    private val androidToast: AndroidToast ?= AndroidToast()
    private val apiService = RetrofitClient.getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
        eventClick()
    }

    private fun eventClick(){
        btnRegister?.setOnClickListener{
            callApiRegister()
        }
    }

    private fun callApiRegister() {
        val email = emailReg?.text.toString().trim()
        val password = passwordReg?.text.toString().trim()
        val fullName = fullNameReg?.text.toString().trim()
        val address = addressReg?.text.toString().trim()
        val phone = phoneReg?.text.toString().trim()

        val userModel = UserModel(email, fullName, password, address, phone)
        Log.d("USERS", userModel.toString())


        if (userModel != null) {
            apiService.register(userModel).enqueue(object : Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Log.d("API_RESPONSE", apiResponse.toString())
                        if (apiResponse != null && apiResponse.status == "Success") {
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            androidToast?.showToast(this@RegisterActivity, "Đăng ký thành công")
                        } else if (apiResponse != null && apiResponse.status == "Failure"){
                            androidToast?.showToast(this@RegisterActivity, "Đăng ký thất bại")
                        }
                    } else {
                        androidToast?.showToast(this@RegisterActivity, "Lỗi")
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    androidToast?.showToast(this@RegisterActivity, "Lỗi")
                }

            })
        } else {
            androidToast?.showToast(this@RegisterActivity, "Nhập đủ thông tin")
        }
    }


    private fun initView(){
        emailReg = findViewById(R.id.emailReg)
        passwordReg = findViewById(R.id.passwordReg)
        fullNameReg = findViewById(R.id.fullNameReg)
        addressReg = findViewById(R.id.addressReg)
        phoneReg = findViewById(R.id.phoneReg)
        btnRegister = findViewById(R.id.btnRegister)

    }
}