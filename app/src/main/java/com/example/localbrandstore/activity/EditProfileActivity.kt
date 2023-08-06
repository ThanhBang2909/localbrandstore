package com.example.localbrandstore.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.localbrandstore.MainActivity
import com.example.localbrandstore.R
import com.example.localbrandstore.model.EditProfileRequest
import com.example.localbrandstore.model.ResponseModel
import com.example.localbrandstore.service.ApiService
import com.example.localbrandstore.service.RetrofitClient
import com.example.localbrandstore.util.AndroidToast
import com.squareup.picasso.Picasso
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var profileFullName: EditText
    private lateinit var profileEmail: EditText
    private lateinit var profilePhone: EditText
    private lateinit var profileAddress: EditText
    private lateinit var btnSaveProfile: Button
    private var userEmail: String = ""
    private var userFullName: String = ""
    private var userAddress: String = ""
    private var userPhone: String = ""
    private val androidToast: AndroidToast?= AndroidToast()
    private val apiService = RetrofitClient.getApiService()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initView()
        eventClick()
        loadProfile()
    }

    private fun eventClick(){

        btnSaveProfile.setOnClickListener{
            userEmail = profileEmail.text.toString().trim()
            userFullName = profileFullName.text.toString().trim()
            userAddress = profileAddress.text.toString().trim()
            userPhone = profilePhone.text.toString().trim()

            callApiUpdateProfile(userEmail, userFullName, userAddress, userPhone)
        }

        btnBack.setOnClickListener{
            finish()
        }

    }

    private fun loadProfile() {
        val bundle = intent.extras
        if (bundle != null) {
            if (!bundle.isEmpty) {
                userEmail = bundle.getString("email", "")
                userFullName = bundle.getString("name", "")
                userAddress = bundle.getString("address", "")
                userPhone = bundle.getString("phone", "")

                profileFullName.setText(userFullName)
                profileAddress.setText(userAddress)
                profilePhone.setText(userPhone)
                profileEmail.setText(userEmail)

            } else {
                androidToast?.showToast(applicationContext, "Không có data")
            }
        }
    }


    private fun callApiUpdateProfile(email: String, fullName: String, address: String, phone: String){
        val profileData = EditProfileRequest(email, fullName, address, phone)
        apiService.editProfilePath(profileData).enqueue(object : Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful){
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status == "Success"){
                        editUserLocalData(email, fullName, address, phone)
                        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        androidToast?.showToast(this@EditProfileActivity, "Fail")
                    }
                }else{
                    androidToast?.showToast(this@EditProfileActivity, "Lỗi")
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                androidToast?.showToast(this@EditProfileActivity, "Call API ERR")
            }

        })
    }

    private fun editUserLocalData(email: String, fullName: String, address: String, phone: String){
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("user_email", email)
        editor.putString("user_fullName", fullName)
        editor.putString("user_address", address)
        editor.putString("user_phone", phone)

        editor.apply()
    }

    private fun initView(){
        btnBack = findViewById(R.id.btnBackProfiePage);
        profileEmail = findViewById(R.id.profile_Email);
        profileFullName = findViewById(R.id.profile_fullName);
        profileAddress = findViewById(R.id.profile_Address);
        profilePhone = findViewById(R.id.profile_phone);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }
}