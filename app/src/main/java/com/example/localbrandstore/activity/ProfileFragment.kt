package com.example.localbrandstore.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.localbrandstore.R
import com.example.localbrandstore.util.AndroidToast


class ProfileFragment : Fragment() {

    private lateinit var editProfile: LinearLayout
    private lateinit var imgAvatar: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var changePassword: TextView
    private lateinit var transactionHistory: TextView
    private lateinit var btnSignOut: Button
    private var userEmail: String = ""
    private var userFullName: String = ""
    private var userPassword: String = ""
    private var userAddress: String = ""
    private var userPhone: String = ""
    private val androidToast: AndroidToast?= AndroidToast()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initView(view)
        eventClick()
        readUserDataLocal()

        return view
    }


    private fun eventClick(){

        editProfile.setOnClickListener{
            val intent = Intent(context, EditProfileActivity::class.java);
            val bundleProfile = Bundle();
            bundleProfile.putString("name", userFullName);
            bundleProfile.putString("address", userAddress);
            bundleProfile.putString("phone", userPhone);
            bundleProfile.putString("email", userEmail);
            intent.putExtras(bundleProfile);
            startActivity(intent);
        }

        changePassword.setOnClickListener{
            val intent = Intent(context, ChangePasswordActivity::class.java);
            val bundleProfile = Bundle();
            bundleProfile.putString("password", userPassword);
            intent.putExtras(bundleProfile);
            startActivity(intent);
        }

    }


    private fun readUserDataLocal(){
        val sharedPreferences = context?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        if (sharedPreferences != null) {
            userEmail = sharedPreferences.getString("user_email", null).toString()
            userFullName = sharedPreferences.getString("user_fullName", null).toString()
            userPassword = sharedPreferences.getString("user_password", null).toString()
            userAddress = sharedPreferences.getString("user_address", null).toString()
            userPhone = sharedPreferences.getString("user_phone", null).toString()
            tvFullName.text = userFullName
        }

    }


    private fun initView(view: View){
        editProfile = view.findViewById(R.id.editProfile);
        imgAvatar = view.findViewById(R.id.imgAvartar);
        tvFullName = view.findViewById(R.id.tvNameCus);
        changePassword = view.findViewById(R.id.changePassword);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        transactionHistory = view.findViewById(R.id.Transaction_history);
    }

}