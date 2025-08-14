package com.example.shopkaro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.shopkaro.data.models.ProductResponse
import com.example.shopkaro.ui.theme.Star

@Composable
fun EnhancedProductCard(
    product: ProductResponse,
    onProductClick: (Int) -> Unit,
    onWishlistClick: (Int) -> Unit,
    onAddToCart: (Int) -> Unit,
    isInWishlist: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(product.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // Wishlist Button
                IconButton(
                    onClick = { onWishlistClick(product.id) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = Color.Red
                    )
                }
                
                // Discount Badge
                if (product.price < 100) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Surface(
                            color = Color.Red,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "SALE",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
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
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${product.price}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (product.price < 100) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "₹${(product.price * 1.2).toInt()}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = Star,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${product.rating.rate}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = " (${product.rating.count})",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                IconButton(
                    onClick = { onAddToCart(product.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.AddShoppingCart,
                        contentDescription = "Add to cart",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}