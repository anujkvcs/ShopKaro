package com.example.shopkaro.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopkaro.data.models.Category
import com.example.shopkaro.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    init {
        loadCategories()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val products = repository.getProducts()
                val categoryMap = products.groupBy { it.category }
                
                val categoryList = categoryMap.map { (categoryName, products) ->
                    Category(
                        id = categoryName,
                        name = categoryName,
                        image = getCategoryImage(categoryName),
                        productCount = products.size
                    )
                }
                
                _categories.value = categoryList
            } catch (e: Exception) {
                _categories.value = emptyList()
            }
        }
    }
    
    private fun getCategoryImage(category: String): String {
        return when (category.lowercase()) {
            "electronics" -> "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=400"
            "jewelery" -> "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400"
            "men's clothing" -> "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400"
            "women's clothing" -> "https://images.unsplash.com/photo-1445205170230-053b83016050?w=400"
            else -> "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=400"
        }
    }
}