package com.example.localbrandstore.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.localbrandstore.MainActivity
import com.example.localbrandstore.R
import com.example.localbrandstore.model.LoginRequest
import com.example.localbrandstore.model.LoginResponse
import com.example.localbrandstore.model.UserModel
import com.example.localbrandstore.service.RetrofitClient
import com.example.localbrandstore.util.AndroidToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences

class LoginActivity : AppCompatActivity() {

    private var emailLogin: EditText ?= null
    private var passwordLogin: EditText ?= null
    private var btnLogin: Button ?= null
    private var btnRegister: Button ?= null
    private var email: String = ""
    private var password: String = ""
    private val apiService = RetrofitClient.getApiService()
    private val androidToast: AndroidToast = AndroidToast()
    private var isFirstTimeLogin: Boolean = false
    private lateinit var sharedPreferencesFirstLogin: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        checkFirstLogin()
        eventClick()
    }

    private fun eventClick(){
        btnRegister?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin?.setOnClickListener{
            email = emailLogin?.text.toString().trim()
            password = passwordLogin?.text.toString().trim()
            callApiLogin(email, password)
        }
    }

    private fun callApiLogin(email: String, password: String) {
        val request = LoginRequest(email, password)
        apiService.login(request).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse = response.body()
                    if (loginResponse?.message == "Success"){
                        val user = loginResponse.user
                        val editor = sharedPreferencesFirstLogin.edit()
                        editor.putBoolean("isFirstTimeLogin", false)
                        editor.apply()
                        saveUserDataLocal(user)
                        navigationToMainActivity()
                        androidToast.showToast(applicationContext, "Đăng nhập thành công")
                    }else{
                        androidToast.showToast(applicationContext, "Login fail")
                    }
                }else{
                    androidToast.showToast(applicationContext, "Lỗi Call API")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                androidToast.showToast(applicationContext,"Call API ERROR")
            }

        })


    }

    private fun saveUserDataLocal(user: UserModel){
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("user_email", user.email)
        editor.putString("user_fullName", user.fullName)
        editor.putString("user_password", user.password)
        editor.putString("user_address", user.address)
        editor.putString("user_phone", user.phone)

        editor.apply()

    }

    private fun checkFirstLogin(){

        sharedPreferencesFirstLogin = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        isFirstTimeLogin =sharedPreferencesFirstLogin.getBoolean("isFirstTimeLogin", true)

        if (isFirstTimeLogin){

        }else{
           navigationToMainActivity()
        }
    }


    private fun navigationToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }


    private fun initView(){
        emailLogin = findViewById(R.id.emailLogin)
        passwordLogin = findViewById(R.id.passwordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnBackRegister)
    }
}
