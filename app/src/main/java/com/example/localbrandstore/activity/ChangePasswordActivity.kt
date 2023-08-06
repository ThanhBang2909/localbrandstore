package com.example.localbrandstore.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.localbrandstore.MainActivity
import com.example.localbrandstore.R
import com.example.localbrandstore.model.ResponseModel
import com.example.localbrandstore.service.RetrofitClient
import com.example.localbrandstore.util.AndroidToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var edtCurrentPassword: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private var password: String = ""
    private var email: String = ""
    private var _currentPassword: String = ""
    private var _newPassword: String = ""
    private var _confirmPassword: String = ""
    private lateinit var btnChangePassword: Button
    private lateinit var btnBack: ImageView
    private val androidToast: AndroidToast?= AndroidToast()
    private val apiService = RetrofitClient.getApiService()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initView()
        eventClick()
    }


    private fun eventClick(){

        btnChangePassword.setOnClickListener{
            _currentPassword = edtCurrentPassword.text.toString().trim()
            _newPassword = edtNewPassword.text.toString().trim()
            _confirmPassword = edtConfirmPassword.text.toString().trim()

            if (checkIsEmpty(_currentPassword, _newPassword, _confirmPassword)) {
                if (checkCurrentPassword(_currentPassword)) {
                    if (checkConfirmPassword(_newPassword, _confirmPassword)) {
                        callApiChangePassword(email, _newPassword)
                    } else {
                        androidToast?.showToast(this, "Mật khẩu không giống nhau")
                    }
                } else {
                    androidToast?.showToast(this, "Mật khẩu không chính xác")
                }
            } else {
                androidToast?.showToast(this, "Vui lòng nhập đầy đủ thông tin")
            }
        }
    }

    private fun callApiChangePassword(email: String, newPassword: String){
        apiService.changePasswordPath(email, newPassword).enqueue(object : Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful){
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status == "Success"){
                        editUserLocalData(newPassword)
                        androidToast?.showToast(this@ChangePasswordActivity, "Đổi mật khẩu thành công")
                        finish()
                    }else{
                        androidToast?.showToast(this@ChangePasswordActivity, "Fail")
                    }
                }else{
                    androidToast?.showToast(this@ChangePasswordActivity, "Lỗi")
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                androidToast?.showToast(this@ChangePasswordActivity, "Call API Error")
            }

        })
    }


    fun checkIsEmpty(currentPassword: String, newPassword: String, confirmPassword: String): Boolean {
        if(currentPassword.isEmpty() && newPassword.isEmpty() && confirmPassword.isEmpty()){
            return false
        }
        return true
    }

    fun checkCurrentPassword(currentPassword: String): Boolean {
        if (currentPassword == password) {
            return true
        }
        return false

    }

    fun checkConfirmPassword(newPassword: String, confirmPassword: String): Boolean {
        if (newPassword == confirmPassword) {
            return true
        }
        return false
    }


    private fun editUserLocalData(password: String){
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("user_password", password)

        editor.apply()
    }


    private fun initView(){
        edtCurrentPassword = findViewById(R.id.currentPassword)
        edtNewPassword = findViewById(R.id.newPassword)
        edtConfirmPassword = findViewById(R.id.confirmPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnBack = findViewById(R.id.btnNavigation)

        val bundle = intent.extras
        if (bundle != null){
            password = bundle.getString("password", "")
            Log.d("pass", password)
        }
    }
}