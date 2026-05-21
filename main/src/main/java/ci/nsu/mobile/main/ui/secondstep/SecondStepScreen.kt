package ci.nsu.mobile.main.ui.secondstep

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.shared.DepositSharedViewModel
import ci.nsu.mobile.main.utils.showToast

@Composable
fun SecondStepScreen(navController: NavController, sharedViewModel: DepositSharedViewModel) {
    val context = LocalContext.current
    val periodMonths = sharedViewModel.periodMonths.value
    val availableRates = remember(periodMonths) {
        when {
            periodMonths == null -> emptyList()
            periodMonths < 6 -> listOf(15.0)
            periodMonths < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
    }
    var selectedRate by remember { mutableStateOf(availableRates.firstOrNull() ?: 0.0) }
    var monthlyTopUp by remember { mutableStateOf("") }

    LaunchedEffect(sharedViewModel.selectedRate.value) {
        sharedViewModel.selectedRate.value?.let { rate ->
            if (availableRates.contains(rate)) selectedRate = rate
        }
    }
    LaunchedEffect(sharedViewModel.monthlyTopUp.value) {
        sharedViewModel.monthlyTopUp.value?.let { topUp ->
            if (topUp != 0.0) monthlyTopUp = topUp.toString()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Дополнительные параметры", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 24.dp))
        if (availableRates.isEmpty()) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Text("Срок не указан. Вернитесь на первый шаг.", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer)
            }
        } else {
            Text("Процентная ставка", modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))
            availableRates.forEach { rate ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    RadioButton(selected = selectedRate == rate, onClick = { selectedRate = rate })
                    Text("$rate%", modifier = Modifier.padding(start = 8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = monthlyTopUp, onValueChange = { monthlyTopUp = it },
                label = { Text("Ежемесячное пополнение (₽) (необязательно)") }, modifier = Modifier.fillMaxWidth(),
                isError = monthlyTopUp.isNotEmpty() && monthlyTopUp.toDoubleOrNull() == null)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) { Text("Назад") }
            Button(onClick = {
                if (availableRates.isEmpty()) { context.showToast("Срок не указан, вернитесь назад"); return@Button }
                val topUp = monthlyTopUp.ifEmpty { null }?.toDoubleOrNull()
                if (monthlyTopUp.isNotEmpty() && topUp == null) { context.showToast("Некорректное значение пополнения"); return@Button }
                sharedViewModel.setSelectedRate(selectedRate)
                sharedViewModel.setMonthlyTopUp(topUp ?: 0.0)
                navController.navigate("result")
            }, enabled = availableRates.isNotEmpty()) { Text("Рассчитать") }
        }
    }
}