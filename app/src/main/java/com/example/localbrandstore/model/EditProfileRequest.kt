package com.example.localbrandstore.model

data class EditProfileRequest(
    val email: String,
    val fullName: String,
    val address: String,
    val phone: String
)
