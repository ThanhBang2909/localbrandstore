package com.example.localbrandstore.util

import android.content.Context
import android.widget.Toast

class AndroidToast {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}