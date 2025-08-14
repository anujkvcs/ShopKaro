package com.example.shopkaro.ui.screens.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopkaro.data.models.ProductResponse
import com.example.shopkaro.data.repository.FirebaseRepo
import com.example.shopkaro.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val firebaseRepo: FirebaseRepo,
    private val networkRepository: NetworkRepository
) : ViewModel() {
    
    private val _wishlistProducts = MutableStateFlow<List<ProductResponse>>(emptyList())
    val wishlistProducts: StateFlow<List<ProductResponse>> = _wishlistProducts.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadWishlistProducts()
    }
    
    private fun loadWishlistProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Get wishlist product IDs from Firebase
                val wishlistIds = firebaseRepo.getWishlistItems()
                
                // Get all products and filter by wishlist IDs
                val allProducts = networkRepository.getProducts()
                val wishlistProducts = allProducts.filter { product ->
                    wishlistIds.contains(product.id.toString())
                }
                
                _wishlistProducts.value = wishlistProducts
            } catch (e: Exception) {
                _wishlistProducts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshWishlist() {
        loadWishlistProducts()
    }
}