package com.example.swipeassignment.model

data class ProductDataResponse(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)