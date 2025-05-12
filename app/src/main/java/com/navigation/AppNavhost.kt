package com.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.data.UserDatabase
import com.repository.UserRepository
import com.starglen.petcarepro.ui.screens.ActivityScreen
import com.starglen.petcarepro.ui.screens.auth.LoginScreen
import com.starglen.petcarepro.ui.screens.auth.RegisterScreen
import com.starglen.petcarepro.ui.screens.dietplan.DietPlanScreen
import com.starglen.petcarepro.ui.screens.health.HealthScreen
import com.starglen.petcarepro.ui.screens.healthhistory.MedicationScreen
import com.starglen.petcarepro.ui.screens.healthhistory.VaccinationScreen
import com.starglen.petcarepro.ui.screens.healthhistory.VetVisitScreen
import com.starglen.petcarepro.ui.screens.home.HomeScreen
import com.starglen.petcarepro.ui.screens.map.MapScreen
import com.starglen.petcarepro.ui.screens.nutrition.NutritionScreen
import com.starglen.petcarepro.ui.screens.reminder.ReminderScreen
import com.starglen.petcarepro.ui.screens.splash.SplashScreen
import com.starglen.petcarepro.ui.screens.supplement.SupplementScreen
import com.viewmodel.ActivityViewModel
import com.viewmodel.AuthViewModel
import com.viewmodel.VisitViewModel
import com.viewmodel.MedicationViewModel
import com.viewmodel.VaccinationViewModel


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH // typically start with splash
) {
    val context = LocalContext.current
    val healthViewModel: VisitViewModel = viewModel()
    val vaccinationViewModel: VaccinationViewModel = viewModel()
    val medicationViewModel: MedicationViewModel = viewModel()
    val activityViewModel: ActivityViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }

        composable(ROUT_HOME) {
            HomeScreen(navController)
        }

        composable(ROUT_REMINDER) {
            ReminderScreen(navController)
        }

        composable(ROUT_HEALTH) {
            HealthScreen(
                navController = navController,
                healthViewModel = healthViewModel,
                vaccinationViewModel = vaccinationViewModel,
                medicationViewModel = medicationViewModel
            )
        }

        composable(ROUT_VET_HISTORY) {
            VetVisitScreen(navController, healthViewModel)
        }

        composable(ROUT_VACCINE) {
            VaccinationScreen(
                navController = navController,
                vaccinationViewModel = vaccinationViewModel
            )
        }

        composable(ROUT_MEDICATION) {
            MedicationScreen(
                navController = navController,
                medicationViewModel = medicationViewModel
            )
        }

        composable(ROUT_ACTIVITY) {
            ActivityScreen(viewModel = activityViewModel, navController = navController)
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
        composable(ROUT_MAP) {
            MapScreen(navController)
        }
// Initialize Room Database and Repository for Authentication
        val appDatabase = UserDatabase.getDatabase(context)
        val authRepository = UserRepository(appDatabase.userDao())
        val authViewModel: AuthViewModel = AuthViewModel(authRepository)
        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        composable(ROUT_LOGIN) {
            LoginScreen(authViewModel, navController) {
                navController.navigate(ROUT_HOME) {
                    popUpTo(ROUT_LOGIN) { inclusive = true }
                }
            }
        }
    }
}