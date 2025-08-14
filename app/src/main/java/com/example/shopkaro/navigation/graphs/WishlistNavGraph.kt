package com.example.shopkaro.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.shopkaro.ui.screens.wishlist.WishlistScreen

fun NavGraphBuilder.wishlistNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.WISHLIST,
        startDestination = "wishlist_screen"
    ) {
        composable(route = "wishlist_screen") {
            WishlistScreen(
                navigateToProduct = { productId ->
                    navController.navigate("product_detail_screen/$productId")
                }
            )
        }
    }
}