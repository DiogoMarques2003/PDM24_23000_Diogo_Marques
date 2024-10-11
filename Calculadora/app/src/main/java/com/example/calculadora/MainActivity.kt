package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadora.ui.theme.CalculadoraTheme

var calcArray: Array<String> = emptyArray()
val operationSymbols = arrayOf("+", "-", "*", "/")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var displayValue = remember { mutableStateOf("") }

    Box(modifier.background(Color.DarkGray)
                .fillMaxSize()
                .padding(top = 12.dp, start = 15.dp, end = 15.dp)) {
        Column (Modifier.fillMaxSize()
                        .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(14.dp)) {

            Text( text = displayValue.value,
                  Modifier.background(Color.White)
                          .fillMaxWidth()
                          .padding(vertical = 10.dp),
                  textAlign = TextAlign.End)

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("MRC", Color.Black) {}
                createButton("M-", Color.Black) {}
                createButton("M+", Color.Black) {}
                createButton("ON/C", Color.Red) {}
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("√", Color.Black) {}
                createButton("%", Color.Black) {}
                createButton("+/-", Color.Black) {}
                createButton("CE", Color.Red) {}
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("7", Color.Gray) { displayValue.value = addNumber("7") }
                createButton("8", Color.Gray) { displayValue.value = addNumber("8") }
                createButton("9", Color.Gray) { displayValue.value = addNumber("9") }
                createButton("÷", Color.Black) { displayValue.value = addSymbol("/", displayValue.value) }
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("4", Color.Gray) { displayValue.value = addNumber("4") }
                createButton("5", Color.Gray) { displayValue.value = addNumber("5") }
                createButton("6", Color.Gray) { displayValue.value = addNumber("6") }
                createButton("X", Color.Black) { displayValue.value = addSymbol("/", displayValue.value) }
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("1", Color.Gray) { displayValue.value = addNumber("1") }
                createButton("2", Color.Gray) { displayValue.value = addNumber("2") }
                createButton("3", Color.Gray) { displayValue.value = addNumber("3") }
                createButton("-", Color.Black) { displayValue.value = addSymbol("/", displayValue.value) }
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("0", Color.Gray) { displayValue.value = addNumber("0") }
                createButton(".", Color.Gray) {}
                createButton("=", Color.Gray) { displayValue.value = addSymbol("/", displayValue.value) }
                createButton("+", Color.Black) { displayValue.value = addSymbol("/", displayValue.value) }
            }
        }
    }
}

@Composable
fun createButton(text: String, color: Color, onClick: () -> Unit) {
    Button(modifier = Modifier.size(width = 80.dp, height = 80.dp),
           onClick = onClick,
           colors = ButtonDefaults.buttonColors(color),
           shape = RoundedCornerShape(35)) {
            Text(text = text, fontSize = 13.sp)
    }
}

fun addNumber(number: String): String {
    // Se não tiver nenhum item no array adicionar o nº como 1º
    if (calcArray.isEmpty()) {
        calcArray += number
        return number
    }

    // Obter o ultimo valor do array
    var lastString = calcArray.last()

    // Se o ultimo valor for um operador adicionar uma nova possição
    if (operationSymbols.contains(lastString)) {
        calcArray += number
        return number
    }

    // Adicionar o novo nº a ultima string
    lastString += number
    calcArray[calcArray.size - 1] = lastString
    return lastString
}

fun addSymbol(symbol: String, lastValue: String): String {
    // Se o array tiver vazio adicionar um 0 e operador
    if (calcArray.isEmpty()) {
        calcArray = arrayOf("0", symbol)
        return "0"
    }

    // Obter o ultimo valor do arrayy
    var lastString = calcArray.last()

    // Se o valor já for um simbolo alterar o memso
    if (operationSymbols.contains(lastString)) {
        calcArray[calcArray.size - 1] = symbol
        return lastValue
    }

    // Adicionar o operador ao array
    calcArray += symbol
    return lastValue
}