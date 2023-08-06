package com.example.localbrandstore.model

data class OrderRequest(
    val customerName: String,
    val phoneNumber: String,
    val customerAddress: String,
    val customerTotalPrice: String,
    val customerStatus: String,
    val email: String
)
