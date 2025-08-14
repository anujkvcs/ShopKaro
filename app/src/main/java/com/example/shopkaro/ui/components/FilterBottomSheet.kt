package com.example.shopkaro.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopkaro.data.models.SearchFilter
import com.example.shopkaro.data.models.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilter: SearchFilter,
    onFilterChange: (SearchFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var minPrice by remember { mutableStateOf(currentFilter.minPrice?.toString() ?: "") }
    var maxPrice by remember { mutableStateOf(currentFilter.maxPrice?.toString() ?: "") }
    var selectedSort by remember { mutableStateOf(currentFilter.sortBy) }
    var selectedRating by remember { mutableStateOf(currentFilter.rating ?: 0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Filter & Sort",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Price Range
        Text("Price Range", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = minPrice,
                onValueChange = { minPrice = it },
                label = { Text("Min Price") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = maxPrice,
                onValueChange = { maxPrice = it },
                label = { Text("Max Price") },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Rating Filter
        Text("Minimum Rating", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = selectedRating,
            onValueChange = { selectedRating = it },
            valueRange = 0f..5f,
            steps = 4
        )
        Text("${selectedRating.toInt()} stars & above")
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sort Options
        Text("Sort By", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        
        SortOption.values().forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedSort == option,
                        onClick = { selectedSort = option }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSort == option,
                    onClick = { selectedSort = option }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    when (option) {
                        SortOption.RELEVANCE -> "Relevance"
                        SortOption.PRICE_LOW_TO_HIGH -> "Price: Low to High"
                        SortOption.PRICE_HIGH_TO_LOW -> "Price: High to Low"
                        SortOption.RATING -> "Customer Rating"
                        SortOption.NEWEST -> "Newest First"
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    onFilterChange(SearchFilter())
                    onDismiss()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear All")
            }
            
            Button(
                onClick = {
                    val filter = SearchFilter(
                        minPrice = minPrice.toDoubleOrNull(),
                        maxPrice = maxPrice.toDoubleOrNull(),
                        rating = if (selectedRating > 0) selectedRating else null,
                        sortBy = selectedSort
                    )
                    onFilterChange(filter)
                    onDismiss()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply")
            }
        }
    }
}