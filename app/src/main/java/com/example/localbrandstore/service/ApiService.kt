package com.example.localbrandstore.service

import com.example.localbrandstore.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

interface ApiService {

    companion object {

        const val url: String = "http://172.20.10.2:8080/localbrandstore/"
        const val imgProductPath: String = url +"products/"
        const val imgSlidePath: String = url + "slide/"
        const val imgCategory: String = url + "category/"
        const val orderPath: String = url + "order.php"
        const val orderDetailPath: String = url + "orderDetail.php"

        private val gson: Gson = GsonBuilder().apply {
            setDateFormat("yyyy-MM-dd HH:mm:ss")
        }.create()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        fun create(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }

    @POST("register.php")
    fun register(
        @Body userModel: UserModel
    ): Call<ResponseModel>

    @POST("login.php")
    fun login(
        @Body requestBody: LoginRequest
    ): Call<LoginResponse>

    @GET("products.php")
    fun getListProducts():Call<List<ProductsModel>>

    @GET("category.php")
    fun getListCategory():Call<List<CategoryModel>>

    @GET("productByCategory.php")
    fun getListProductsByCategory(
        @Query("catID") catID: String
    ): Call<List<ProductsModel>>

    @POST("order.php")
    fun orderPath(
        @Body orderRequest: OrderRequest
    ): Call<ResponseModel>

    @PUT("editProfile.php")
    fun editProfilePath(
        @Body dataRequest: EditProfileRequest
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("changePassword.php")
    fun changePasswordPath(
        @Field("email") email: String,
        @Field("newPassword") newPassword: String
    ): Call<ResponseModel>

}