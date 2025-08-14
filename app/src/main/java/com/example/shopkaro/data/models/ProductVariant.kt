package com.example.shopkaro.data.models

data class ProductVariant(
    val id: String,
    val size: String? = null,
    val color: String? = null,
    val price: Double,
    val stock: Int,
    val image: String? = null
)

data class Review(
    val id: String,
    val userId: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String,
    val images: List<String> = emptyList()
)

data class WishlistItem(
    val productId: Int,
    val userId: String,
    val addedDate: String
)