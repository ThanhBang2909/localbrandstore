package com.example.localbrandstore.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.localbrandstore.MainActivity

class AndroidUtil {

    fun navigationToMainActivity(context: Context){
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}