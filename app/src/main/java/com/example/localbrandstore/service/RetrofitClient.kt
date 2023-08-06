package com.example.localbrandstore.service

object RetrofitClient {
    private val apiService: ApiService = ApiService.create()

    fun getApiService() : ApiService{
        return apiService
    }
}