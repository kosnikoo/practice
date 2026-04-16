package Colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ColorPickerScreen(
    viewModel: ColorPickerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(uiState.color)
        )

        Text(
            text = uiState.hexCode,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        ColorSlider(
            label = "Red",
            value = uiState.red.toFloat(),
            onValueChange = { viewModel.onRedChanged(it) },
            color = Color.Red
        )

        ColorSlider(
            label = "Green",
            value = uiState.green.toFloat(),
            onValueChange = { viewModel.onGreenChanged(it) },
            color = Color.Green
        )

        ColorSlider(
            label = "Blue",
            value = uiState.blue.toFloat(),
            onValueChange = { viewModel.onBlueChanged(it) },
            color = Color.Blue
        )

        Button(
            onClick = { viewModel.generateRandomColor() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Случайный цвет")
        }
    }
}

@Composable
fun ColorSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    color: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label: ${value.toInt()}",
            color = color,
            fontWeight = FontWeight.Medium
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color
            )
        )
    }
}