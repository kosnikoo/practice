package ci.nsu.mobile.main.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import ci.nsu.mobile.main.utils.showToast
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(navController: NavController, calculationId: Long) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        val dao = AppDatabase.getDatabase(context.applicationContext).depositDao()
        DepositRepository.getInstance(dao)
    }
    var calculation by remember { mutableStateOf<DepositCalculation?>(null) }

    LaunchedEffect(calculationId) {
        scope.launch {
            calculation = repository.getCalculationById(calculationId)
            if (calculation == null) {
                context.showToast("Расчёт не найден")
                navController.popBackStack()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Детали расчёта") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") }
        })
        if (calculation == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow("Дата расчёта:", calculation!!.getFormattedDate())
                        DetailRow("Стартовый взнос:", "%.2f руб.".format(calculation!!.initialAmount))
                        DetailRow("Срок:", "${calculation!!.periodMonths} мес.")
                        DetailRow("Процентная ставка:", "%.2f%%".format(calculation!!.interestRate))
                        DetailRow("Ежемесячное пополнение:", calculation!!.monthlyTopUp?.let { "%.2f руб.".format(it) } ?: "—")
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        DetailRow("Итоговая сумма:", "%.2f руб.".format(calculation!!.finalAmount), isBold = true, color = MaterialTheme.colorScheme.primary)
                        DetailRow("Начисленные проценты:", "%.2f руб.".format(calculation!!.interestEarned))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isBold: Boolean = false, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isBold) androidx.compose.ui.text.font.FontWeight.Bold else null))
        Text(value, style = MaterialTheme.typography.bodyLarge, color = color)
    }
}