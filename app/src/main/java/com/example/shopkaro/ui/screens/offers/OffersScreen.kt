package com.example.shopkaro.ui.screens.offers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

data class Offer(
    val id: String,
    val title: String,
    val description: String,
    val discount: String,
    val image: String,
    val validUntil: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    onBackClick: () -> Unit
) {
    val offers = remember {
        listOf(
            Offer("1", "Electronics Sale", "Up to 70% off on smartphones and laptops", "70% OFF", "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=400", "Valid till 31st Dec"),
            Offer("2", "Fashion Week", "Trendy clothes at unbeatable prices", "50% OFF", "https://images.unsplash.com/photo-1445205170230-053b83016050?w=400", "Valid till 25th Dec"),
            Offer("3", "Jewelry Collection", "Elegant jewelry for special occasions", "40% OFF", "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400", "Valid till 28th Dec")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Special Offers") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(offers) { offer ->
                OfferCard(offer = offer)
            }
        }
    }
}

@Composable
fun OfferCard(offer: Offer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(offer.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            Color.Red,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        offer.discount,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    offer.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    offer.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        offer.validUntil,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    Button(
                        onClick = { },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Shop Now")
                    }
                }
            }
        }
    }
}