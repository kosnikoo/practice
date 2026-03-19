package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ColorSearchScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

val colorMap = mapOf(
    "red" to Color.Red,
    "blue" to Color.Blue,
    "green" to Color.Green,
    "yellow" to Color.Yellow,
    "cyan" to Color.Cyan,
    "magenta" to Color.Magenta,
    "black" to Color.Black,
    "white" to Color.White,
    "gray" to Color.Gray
)

@Composable
fun ColorSearchScreen(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf<Color?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите цвет") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val searchKey = inputText.trim().lowercase()
                val found = colorMap[searchKey]
                if (found != null) {
                    buttonColor = found
                } else {
                    Log.d("ColorSearch", "Цвет \"$searchKey\" не найден")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor ?: MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Найти цвет")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Палитра цветов
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colorMap.toList()) { (name, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = name)
                }
            }
        }
    }
}