package com.example.shopkaro.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import com.example.shopkaro.ui.components.FilterBottomSheet
import com.example.shopkaro.data.models.SearchFilter
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopkaro.data.models.ProductResponse
import com.example.shopkaro.ui.screens.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToProduct: (id: Int) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState(initial = emptySet())
    val isLoading by viewModel.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var showFilterSheet by remember { mutableStateOf(false) }
    var currentFilter by remember { mutableStateOf(SearchFilter()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = { Text("Search products...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Row {
                            IconButton(onClick = { showFilterSheet = true }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filter")
                            }
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.performSearch()
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        when {
            searchQuery.isEmpty() -> {
                // Show search history and suggestions
                LazyColumn {
                    if (searchHistory.isNotEmpty()) {
                        item {
                            Text(
                                "Recent Searches",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        items(searchHistory.toList()) { query ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectSearchHistory(query) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.History,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(query, modifier = Modifier.weight(1f))
                                Icon(
                                    Icons.Default.NorthWest,
                                    contentDescription = "Use search",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
            
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            else -> {
                // Show search results
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { product ->
                        Product(product = product, navigateToProduct = navigateToProduct)
                    }
                }
            }
        }
        
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false }
            ) {
                FilterBottomSheet(
                    currentFilter = currentFilter,
                    onFilterChange = { filter ->
                        currentFilter = filter
                        viewModel.applyFilter(filter)
                    },
                    onDismiss = { showFilterSheet = false }
                )
            }
        }
    }
}