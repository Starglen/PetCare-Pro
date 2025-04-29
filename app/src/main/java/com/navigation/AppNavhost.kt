package com.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.starglen.petcarepro.ui.screens.ActivityScreen
import com.starglen.petcarepro.ui.screens.dietplan.DietPlanScreen
import com.starglen.petcarepro.ui.screens.health.HealthScreen
import com.starglen.petcarepro.ui.screens.home.HomeScreen
import com.starglen.petcarepro.ui.screens.nutrition.NutritionScreen
import com.starglen.petcarepro.ui.screens.nutrition.ROUTE_SUPPLEMENTS
import com.starglen.petcarepro.ui.screens.reminder.ReminderScreen
import com.starglen.petcarepro.ui.screens.splash.SplashScreen
import com.starglen.petcarepro.ui.screens.supplement.SupplementScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_HOME
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }

        composable(ROUT_REMINDER) {
            ReminderScreen(navController)
        }

        composable(ROUT_HEALTH) {
            HealthScreen(navController)
        }

        composable(ROUT_ACTIVITY) {
            ActivityScreen(navController)
        }

        composable(ROUT_NUTRITION) {
            NutritionScreen(navController)
        }

        composable(ROUT_DIET) {
            DietPlanScreen(navController)
        }

        composable(ROUT_SUPPLEMENTS) {
            SupplementScreen(navController)
        }

        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }

    }
}