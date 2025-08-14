package com.example.shopkaro.data.models

data class Category(
    val id: String,
    val name: String,
    val image: String,
    val productCount: Int = 0
)