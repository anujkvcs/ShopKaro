package com.example.shopkaro.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopkaro.data.models.ProductResponse
import com.example.shopkaro.data.models.SearchFilter
import com.example.shopkaro.data.models.SortOption
import com.example.shopkaro.data.preferences.PreferencesManager
import com.example.shopkaro.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: NetworkRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<ProductResponse>>(emptyList())
    val searchResults: StateFlow<List<ProductResponse>> = _searchResults.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private var currentFilter = SearchFilter()
    private var allProducts = emptyList<ProductResponse>()
    
    val searchHistory = preferencesManager.getSearchHistory()
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.length >= 2) {
            performSearch()
        }
    }
    
    fun performSearch() {
        val query = _searchQuery.value.trim()
        if (query.isEmpty()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                allProducts = repository.getProducts()
                val filteredProducts = allProducts.filter { product ->
                    product.title.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
                }
                _searchResults.value = applyFiltersAndSort(filteredProducts)
                preferencesManager.addSearchQuery(query)
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectSearchHistory(query: String) {
        _searchQuery.value = query
        performSearch()
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }
    
    fun applyFilter(filter: SearchFilter) {
        currentFilter = filter
        if (_searchResults.value.isNotEmpty()) {
            _searchResults.value = applyFiltersAndSort(_searchResults.value)
        }
    }
    
    private fun applyFiltersAndSort(products: List<ProductResponse>): List<ProductResponse> {
        var filtered = products
        
        // Apply price filter
        currentFilter.minPrice?.let { min ->
            filtered = filtered.filter { it.price >= min }
        }
        currentFilter.maxPrice?.let { max ->
            filtered = filtered.filter { it.price <= max }
        }
        
        // Apply rating filter
        currentFilter.rating?.let { minRating ->
            filtered = filtered.filter { it.rating.rate >= minRating }
        }
        
        // Apply sorting
        return when (currentFilter.sortBy) {
            SortOption.PRICE_LOW_TO_HIGH -> filtered.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> filtered.sortedByDescending { it.price }
            SortOption.RATING -> filtered.sortedByDescending { it.rating.rate }
            else -> filtered
        }
    }
}