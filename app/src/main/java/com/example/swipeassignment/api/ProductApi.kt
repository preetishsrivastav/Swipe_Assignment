package com.example.swipeassignment.api

import com.example.swipeassignment.model.ProductDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {
    @GET("/get")
    suspend fun getProducts():Response<List<ProductDataResponse>>
}