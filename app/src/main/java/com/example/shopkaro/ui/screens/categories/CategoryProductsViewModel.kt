package com.example.shopkaro.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopkaro.data.models.ProductResponse
import com.example.shopkaro.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {
    
    private val _products = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allProducts = repository.getProducts()
                val filteredProducts = allProducts.filter { 
                    it.category.equals(category, ignoreCase = true) 
                }
                _products.value = filteredProducts
            } catch (e: Exception) {
                _products.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}