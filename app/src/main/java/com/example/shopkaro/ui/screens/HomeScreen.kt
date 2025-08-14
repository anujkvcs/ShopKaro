package com.example.shopkaro.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.shopkaro.data.models.ProductResponse
import com.example.shopkaro.ui.theme.Star


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navigateToProduct: (id: Int) -> Unit,
    navigateToSearch: () -> Unit = {},
    navigateToCategories: () -> Unit = {}
) {
    val viewmodel: HomeViewModel = hiltViewModel()
    val products = viewmodel.products.collectAsState()
    val categories = viewmodel.categories.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ShopKaro", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = navigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* Navigate to notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search Bar
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToSearch() },
                    colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Search for products...",
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Banner
            item(span = { GridItemSpan(2) }) {
                Image(
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=800"),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Categories Section
            item(span = { GridItemSpan(2) }) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Categories",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = navigateToCategories) {
                            Text("View All")
                        }
                    }
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categories.value.take(4)) { category ->
                            CategoryChip(category = category.name)
                        }
                    }
                }
            }
            
            // Products Section Header
            item(span = { GridItemSpan(2) }) {
                Text(
                    "Featured Products",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Products
            items(products.value) { product ->
                Product(product = product, navigateToProduct = navigateToProduct)
            }
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Card(
        modifier = Modifier.width(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = category.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}


@Composable
fun Product(product: ProductResponse, navigateToProduct: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToProduct(product.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box {
                Image(
                    rememberAsyncImagePainter(product.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // Wishlist icon
                IconButton(
                    onClick = { /* TODO: Add to wishlist */ },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Add to wishlist",
                        tint = Color.Red
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = product.title,
                maxLines = 2,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${product.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = Star,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${product.rating.rate}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}