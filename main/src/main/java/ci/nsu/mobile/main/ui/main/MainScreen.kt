package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Расчёт вкладов", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 48.dp))
        Button(onClick = { navController.navigate("firstStep") }, modifier = Modifier.fillMaxWidth(0.8f).padding(8.dp)) {
            Text("Рассчитать")
        }
        Button(onClick = { navController.navigate("history") }, modifier = Modifier.fillMaxWidth(0.8f).padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Text("История расчётов")
        }
        Button(onClick = { android.os.Process.killProcess(android.os.Process.myPid()) }, modifier = Modifier.fillMaxWidth(0.8f).padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
            Text("Закрыть приложение")
        }
    }
}