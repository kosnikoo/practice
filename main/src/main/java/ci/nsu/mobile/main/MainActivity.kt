package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.detail.DetailScreen
import ci.nsu.mobile.main.ui.firststep.FirstStepScreen
import ci.nsu.mobile.main.ui.history.HistoryScreen
import ci.nsu.mobile.main.ui.main.MainScreen
import ci.nsu.mobile.main.ui.result.ResultScreen
import ci.nsu.mobile.main.ui.secondstep.SecondStepScreen
import ci.nsu.mobile.main.ui.shared.DepositSharedViewModel
import ci.nsu.mobile.main.ui.theme.DepositCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DepositCalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val sharedViewModel: DepositSharedViewModel = viewModel()
                    NavHost(navController, startDestination = "main") {
                        composable("main") { MainScreen(navController) }
                        composable("firstStep") { FirstStepScreen(navController, sharedViewModel) }
                        composable("secondStep") { SecondStepScreen(navController, sharedViewModel) }
                        composable("result") { ResultScreen(navController, sharedViewModel) }
                        composable("history") { HistoryScreen(navController) }
                        composable("detail/{calculationId}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("calculationId")?.toLongOrNull() ?: 0L
                            DetailScreen(navController, id)
                        }
                    }
                }
            }
        }
    }
}