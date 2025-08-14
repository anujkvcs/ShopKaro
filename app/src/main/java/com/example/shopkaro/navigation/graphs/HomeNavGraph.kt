package com.example.shopkaro.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.shopkaro.ui.screens.HomeScreen
import com.example.shopkaro.ui.screens.product_detail.ProductDetailScreen
import com.example.shopkaro.ui.screens.product_detail.ProductDetailViewModel
import com.example.shopkaro.ui.screens.search.SearchScreen
import com.example.shopkaro.ui.screens.categories.CategoriesScreen
import com.example.shopkaro.ui.screens.categories.CategoryProductsScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController, modifier: Modifier) {
    navigation(
        route = Graph.HOME,
        startDestination = HomeScreens.HomeScreen.route,
    ) {
        composable(HomeScreens.HomeScreen.route) {
            HomeScreen(
                modifier = modifier,
                navigateToProduct = { id ->
                    navController.navigate(HomeScreens.ProductDetailScreen.passArgs(id))
                },
                navigateToSearch = {
                    navController.navigate(HomeScreens.SearchScreen.route)
                },
                navigateToCategories = {
                    navController.navigate(HomeScreens.CategoriesScreen.route)
                }
            )
        }
        composable(
            HomeScreens.ProductDetailScreen.route, arguments = listOf(
            navArgument("productId") {
                type = NavType.IntType
            }
        )) {
            val productDetailViewModel: ProductDetailViewModel = hiltViewModel()
            val productDetailState = productDetailViewModel.productDetailUiState.collectAsState()
            ProductDetailScreen(
                productDetailState = productDetailState.value,
                navigateToCart = {
                    navController.navigate(Graph.CART)
                },
                addToCart = { productId -> productDetailViewModel.addToCart(productId) },
                removeFromCart = { productId -> productDetailViewModel.removeFromCart(productId) }
            )
        }
        
        composable(HomeScreens.SearchScreen.route) {
            SearchScreen(
                navigateToProduct = { id ->
                    navController.navigate(HomeScreens.ProductDetailScreen.passArgs(id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(HomeScreens.CategoriesScreen.route) {
            CategoriesScreen(
                navigateToCategory = { category ->
                    navController.navigate(HomeScreens.CategoryProductsScreen.passArgs(category))
                }
            )
        }
        
        composable(
            HomeScreens.CategoryProductsScreen.route,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            CategoryProductsScreen(
                category = category,
                navigateToProduct = { id ->
                    navController.navigate(HomeScreens.ProductDetailScreen.passArgs(id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class HomeScreens(val route: String) {
    object HomeScreen : HomeScreens("home")
    object ProductDetailScreen : HomeScreens("product_detail/{productId}") {
        fun passArgs(id: Int): String {
            return "product_detail/$id"
        }
    }
    object SearchScreen : HomeScreens("search")
    object CategoriesScreen : HomeScreens("categories")
    object CategoryProductsScreen : HomeScreens("category_products/{category}") {
        fun passArgs(category: String): String {
            return "category_products/$category"
        }
    }
}
