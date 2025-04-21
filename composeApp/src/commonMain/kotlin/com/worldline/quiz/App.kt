package com.worldline.quiz

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.worldline.quiz.data.models.BetViewModel
import com.worldline.quiz.data.models.LoginViewModel
import com.worldline.quiz.screens.AddBetScreen
import com.worldline.quiz.screens.BetDetailsScreen
import com.worldline.quiz.screens.HomeScreen
import com.worldline.quiz.screens.StatsScreen
import com.worldline.quiz.screens.welcomeScreen

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    betViewModel: BetViewModel,
    loginViewModel: LoginViewModel // Nouveau paramètre

) {
    MaterialTheme {
        NavHost(navController = navController, startDestination = "/welcome") {
            composable("/welcome") {
                welcomeScreen(onStartButtonPushed = {
                    navController.navigate("/home")
                })
            }
            composable("/home") {
                HomeScreen(
                    viewModel = betViewModel,
                    onAddBet = { navController.navigate("/add") },
                    onViewStats = { navController.navigate("/stats") },
                    onBetSelected = { betId -> navController.navigate("/details/$betId") },
                    onLogout = {
                        loginViewModel.logout()
                        betViewModel.clearSession()
                        navController.navigate("/welcome") {
                            popUpTo("/home") { inclusive = true }
                        }
                    }
                )
            }
            composable("/add") {
                AddBetScreen(viewModel = betViewModel, onBetAdded = {
                    navController.popBackStack("/home", inclusive = false)
                })
            }
            composable("/stats") {
                StatsScreen(viewModel = betViewModel, onBack = {
                    navController.popBackStack("/home", inclusive = false)
                })
            }
            composable("/details/{betId}") { backStackEntry ->
                val betId =
                    backStackEntry.arguments?.getString("betId")?.toIntOrNull()
                        ?: return@composable
                BetDetailsScreen(betId = betId, viewModel = betViewModel, onBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}

