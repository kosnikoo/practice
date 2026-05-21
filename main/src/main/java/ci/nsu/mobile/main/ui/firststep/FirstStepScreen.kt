package ci.nsu.mobile.main.ui.firststep

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
fun FirstStepScreen(navController: NavController, sharedViewModel: DepositSharedViewModel) {
    var initialAmount by remember { mutableStateOf("") }
    var periodMonths by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(sharedViewModel.initialAmount.value, sharedViewModel.periodMonths.value) {
        sharedViewModel.initialAmount.value?.let { initialAmount = it.toString() }
        sharedViewModel.periodMonths.value?.let { periodMonths = it.toString() }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Основные параметры", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 24.dp))
        OutlinedTextField(value = initialAmount, onValueChange = { initialAmount = it }, label = { Text("Стартовый взнос (₽)") },
            modifier = Modifier.fillMaxWidth(),
            isError = initialAmount.isNotEmpty() && (initialAmount.toDoubleOrNull() == null || initialAmount.toDoubleOrNull()!! <= 0))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = periodMonths, onValueChange = { periodMonths = it }, label = { Text("Срок (месяцы)") },
            modifier = Modifier.fillMaxWidth(),
            isError = periodMonths.isNotEmpty() && (periodMonths.toIntOrNull() == null || periodMonths.toIntOrNull()!! <= 0))
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { sharedViewModel.clear(); navController.navigate("main") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Text("В начало")
            }
            Button(onClick = {
                val amount = initialAmount.toDoubleOrNull()
                val months = periodMonths.toIntOrNull()
                if (amount == null || amount <= 0) { context.showToast("Введите корректный стартовый взнос (>0)"); return@Button }
                if (months == null || months <= 0) { context.showToast("Введите корректный срок (>0)"); return@Button }
                sharedViewModel.setInitialAmount(amount)
                sharedViewModel.setPeriodMonths(months)
                navController.navigate("secondStep")
            }) { Text("Далее") }
        }
    }
}