package ci.nsu.mobile.main.ui.result

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.domain.DepositCalculator
import ci.nsu.mobile.main.ui.shared.DepositSharedViewModel
import ci.nsu.mobile.main.utils.showToast
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(navController: NavController, sharedViewModel: DepositSharedViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        val dao = AppDatabase.getDatabase(context.applicationContext).depositDao()
        DepositRepository.getInstance(dao)
    }
    val initialAmount = sharedViewModel.initialAmount.value ?: 0.0
    val periodMonths = sharedViewModel.periodMonths.value ?: 0
    val monthlyTopUp = sharedViewModel.monthlyTopUp.value ?: 0.0
    val interestRate = sharedViewModel.selectedRate.value ?: 0.0
    val (finalAmount, interestEarned) = DepositCalculator.calculate(initialAmount, periodMonths, interestRate, monthlyTopUp)
    var saved by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Результат расчёта", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                ResultRow("Стартовый взнос:", "%.2f руб.".format(initialAmount))
                ResultRow("Срок:", "$periodMonths мес.")
                ResultRow("Процентная ставка:", "%.2f%%".format(interestRate))
                ResultRow("Ежемесячное пополнение:", if (monthlyTopUp > 0) "%.2f руб.".format(monthlyTopUp) else "—")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ResultRow("Итоговая сумма:", "%.2f руб.".format(finalAmount), isBold = true, color = MaterialTheme.colorScheme.primary)
                ResultRow("Начисленные проценты:", "%.2f руб.".format(interestEarned))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                if (!saved) {
                    scope.launch {
                        repository.saveCalculation(DepositCalculation(
                            initialAmount = initialAmount,
                            periodMonths = periodMonths,
                            interestRate = interestRate,
                            monthlyTopUp = if (monthlyTopUp == 0.0) null else monthlyTopUp,
                            finalAmount = finalAmount,
                            interestEarned = interestEarned
                        ))
                        saved = true
                        context.showToast("Расчёт сохранён")
                    }
                } else context.showToast("Уже сохранено")
            }) { Text("Сохранить") }
            Button(onClick = { sharedViewModel.clear(); navController.navigate("main") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) { Text("В начало") }
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, isBold: Boolean = false, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isBold) androidx.compose.ui.text.font.FontWeight.Bold else null))
        Text(value, style = MaterialTheme.typography.bodyLarge, color = color)
    }
}