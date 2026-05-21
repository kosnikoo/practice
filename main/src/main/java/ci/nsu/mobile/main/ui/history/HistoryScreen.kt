package ci.nsu.mobile.main.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        val dao = AppDatabase.getDatabase(context.applicationContext).depositDao()
        DepositRepository.getInstance(dao)
    }
    var calculations by remember { mutableStateOf<List<DepositCalculation>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            repository.getAllCalculations().collectLatest { calculations = it }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("История расчётов") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") }
        })
        if (calculations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Нет сохранённых расчётов") }
        } else {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(calculations) { calc ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { navController.navigate("detail/${calc.id}") }, elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(calc.getFormattedDate(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                            Text("Стартовый взнос: %.2f руб.".format(calc.initialAmount), style = MaterialTheme.typography.bodyLarge)
                            Text("Итог: %.2f руб.".format(calc.finalAmount), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}