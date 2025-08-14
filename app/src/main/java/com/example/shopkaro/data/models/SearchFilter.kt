package com.example.shopkaro.data.models

data class SearchFilter(
    val category: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val rating: Float? = null,
    val sortBy: SortOption = SortOption.RELEVANCE
)

enum class SortOption {
    RELEVANCE,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    RATING,
    NEWEST
}