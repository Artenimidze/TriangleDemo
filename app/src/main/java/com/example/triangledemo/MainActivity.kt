package com.example.triangledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triangledemo.ui.theme.TriangleDemoTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriangleDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

data class TriangleResult(
    val leg: Double,
    val hypotenuse: Double,
    val area: Double,
    val error: String? = null
)

fun triangleCalculator(elementType: String, value: Double): TriangleResult {
    if (value <= 0) {
        return TriangleResult(0.0, 0.0, 0.0, "Значение должно быть положительным")
    }

    val key = elementType.trim().lowercase().firstOrNull()
        ?: return TriangleResult(0.0, 0.0, 0.0, "Введите букву элемента")

    return when (key) {
        'к', 'k' -> {
            val a = value
            TriangleResult(
                leg = a,
                hypotenuse = a * sqrt(2.0),
                area = a * a / 2.0
            )
        }
        'г', 'g', 'h' -> {
            val c = value
            val a = c / sqrt(2.0)
            TriangleResult(
                leg = a,
                hypotenuse = c,
                area = c * c / 4.0
            )
        }
        'п', 'p', 's' -> {
            val s = value
            val a = sqrt(2.0 * s)
            TriangleResult(
                leg = a,
                hypotenuse = 2.0 * sqrt(s),
                area = s
            )
        }
        else -> TriangleResult(0.0, 0.0, 0.0,
            "Неизвестный элемент. Используйте: к (катет), г (гипотенуза), п (площадь)")
    }
}
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var elementType by remember { mutableStateOf("") }
    var textField by remember { mutableStateOf("") }
    var valueText by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<TriangleResult?>(null) }
    var inputError by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        Text("Введите первую букву элемента и его значение:",
            style = MaterialTheme.typography.bodyLarge)
        Text("где к - катет, г - гипотенуза, п - площадь",
            style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = textField,
            onValueChange = {
                textField = it
                elementType = it.take(1)
                valueText = it.drop(1)
                            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        Button(
            onClick = {
                inputError = null
                result = null
                val v = valueText.replace(',', '.').toDoubleOrNull()
                if (v == null) {
                    inputError = "Введите корректное число"
                } else if (elementType.isBlank()) {
                    inputError = "Введите букву элемента"
                } else {
                    val r = triangleCalculator(elementType, v)
                    if (r.error != null) inputError = r.error else result = r
                }
            }
        ) {
            Text("Вычислить")
        }

        inputError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        result?.let { r ->
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Результат", style = MaterialTheme.typography.titleMedium)
                    Text("Катет:       ${"%.4f".format(r.leg)}")
                    Text("Гипотенуза:  ${"%.4f".format(r.hypotenuse)}")
                    Text("Площадь:     ${"%.4f".format(r.area)}")
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
fun TrianglePreview () {
    MainScreen()
}