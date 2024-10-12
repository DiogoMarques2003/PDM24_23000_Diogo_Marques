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
import kotlin.math.sqrt

// Variaveis globais
var calcArray: Array<String> = emptyArray()

// Constantes
val operationSymbols = arrayOf("+", "-", "*", "/", "%")

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
                          .padding(vertical = 13.dp),
                  textAlign = TextAlign.End,
                  fontSize = 20.sp)

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("MRC", Color.Black) {}
                createButton("M-", Color.Black) {}
                createButton("M+", Color.Black) {}
                createButton("ON/C", Color.Red) {}
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("√", Color.Black) { displayValue.value = addSquareRoot() }
                createButton("%", Color.Black) { displayValue.value = addSymbol("%", displayValue.value) }
                createButton("+/-", Color.Black) { displayValue.value = toggleSign() }
                createButton("CE", Color.Red) { displayValue.value = clearInfos() }
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
                createButton("X", Color.Black) { displayValue.value = addSymbol("*", displayValue.value) }
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("1", Color.Gray) { displayValue.value = addNumber("1") }
                createButton("2", Color.Gray) { displayValue.value = addNumber("2") }
                createButton("3", Color.Gray) { displayValue.value = addNumber("3") }
                createButton("-", Color.Black) { displayValue.value = addSymbol("-", displayValue.value) }
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("0", Color.Gray) { displayValue.value = addNumber("0") }
                createButton(".", Color.Gray) { displayValue.value = addDot() }
                createButton("=", Color.Gray) { displayValue.value = calcResult() }
                createButton("+", Color.Black) { displayValue.value = addSymbol("+", displayValue.value) }
            }
        }
    }
}

@Composable
fun createButton(text: String, color: Color, onClick: () -> Unit) {
    Button(modifier = Modifier.size(width = 80.dp, height = 50.dp),
           onClick = onClick,
           colors = ButtonDefaults.buttonColors(color),
           shape = RoundedCornerShape(35)) {
            Text(text = text, fontSize = 13.sp)
    }
}

fun clearInfos(): String {
    calcArray = emptyArray()
    return ""
}

fun addNumber(number: String): String {
    // Se não tiver nenhum item no array ou tiver dado erro adicionar o nº como 1º
    if (calcArray.isEmpty() || calcArray.first() == "ERR") {
        calcArray = arrayOf(number)
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
    // Se o array tiver vazio ou tiver dado um erro adicionar um 0 e operador
    if (calcArray.isEmpty() || calcArray.first() == "ERR") {
        calcArray = arrayOf("0", symbol)
        return "0"
    }

    // Se o array já tiver 3 possições fazer o calculo e colocar o resultado e simbolo no array
    if (calcArray.size == 3) {
        var result = calcResult()
        calcArray = arrayOf(result, symbol)
        return result
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

fun addDot(): String {
    // Se não tiver nenhum item no array ou tiver dado erro adicionar 0.
    if (calcArray.isEmpty() || calcArray.first() == "ERR") {
        calcArray = arrayOf("0.")
        return "0."
    }

    // Obter o ultimo valor do array
    var lastString = calcArray.last()

    // Se o ultimo valor for um operador adicionar 0.
    if (operationSymbols.contains(lastString)) {
        calcArray += "0."
        return "0."
    }

    // Se já tiver ponto não fazer nada
    if (lastString.contains(".")) return lastString

    // Adicionar o ponto ao número
    lastString += "."
    calcArray[calcArray.size - 1] = lastString
    return lastString
}

fun addSquareRoot(): String {
    // Se não tiver nenhum item no array ou tiver dado erro adicionar o simbolo de raiz
    if (calcArray.isEmpty() || calcArray.first() == "ERR") {
        calcArray = arrayOf("√")
        return "√"
    }

    // Obter o ultimo valor do array
    var lastString = calcArray.last()

    // Se o valor já for um simbolo alterar o memso
    if (operationSymbols.contains(lastString)) {
        calcArray += "√"
        return "√"
    }

    // Se não for no inicio do calculo não se pode adicionar a raiz
    return lastString
}

fun toggleSign(): String {
    var numValue: String

    // Se não tiver nenhum item no array ou tiver dado erro adicionar o simbolo de negativo
    if (calcArray.isEmpty() || calcArray.first() == "ERR") {
        calcArray = arrayOf("-0")
        return "-0"
    }

    // Alterar o sinal do ultimo elemento
    if (calcArray.size == 3) {
        numValue = calcArray.last()
        numValue = if (numValue.contains("-")) numValue.replace("-", "") else "-$numValue"
        calcArray[calcArray.size - 1] = numValue
        return numValue
    }

    // Se não alterar o sinal do 1º elemento
    numValue = calcArray.first()
    numValue = if (numValue.contains("-")) numValue.replace("-", "") else "-$numValue"
    calcArray[0] = numValue
    return numValue
}

fun calcResult(): String {
    var firstNumber: Double
    var operationValue: String

    // Se o array estiver vazio não devolver nada
    if (calcArray.isEmpty()) return ""

    // Se o tamanho for 1 devolver o valor do array
    if (calcArray.size == 1) {
        firstNumber = resolveSquareRoot(calcArray.first())
        return removeZeros(firstNumber)
    }

    // Se tiver dois elementos fazer a operação com o 1º e o operador
    if (calcArray.size == 2) {
        firstNumber = resolveSquareRoot(calcArray.first())
        operationValue = operatorResolve(firstNumber, calcArray[1], firstNumber)
        // Colocar o resultado no array
        calcArray = arrayOf(operationValue)
        return operationValue
    }

    // Fazer o calculo de quanto tem as 3 possições
    firstNumber = resolveSquareRoot(calcArray.first())
    var secondNumber = resolveSquareRoot(calcArray.last())
    operationValue = operatorResolve(firstNumber, calcArray[1], secondNumber)
    // Colocar o resultado no array
    calcArray = arrayOf(operationValue)
    return operationValue
}

fun operatorResolve(firstNumber: Double, operator: String, secondNumber: Double = 0.0): String {
    if (operator == "/" && secondNumber == 0.0) {
        return "ERR"
    }

    var result = when(operator) {
        "+" -> firstNumber + secondNumber
        "-" -> firstNumber - secondNumber
        "*" -> firstNumber * secondNumber
        "/" -> firstNumber / secondNumber
        else -> 0
    }

    return removeZeros(result)
}

fun resolveSquareRoot(number: String): Double {
    if (!number.startsWith("√")) return number.toDouble()

    // Remover o simbolo da raiz e devolver o calculo
    var numberDouble = number.replace("√", "").toDouble()
    return sqrt(numberDouble)
}

fun removeZeros(number: Number): String {
    var resultString = number.toString()
    // Se acabar por .0 devolver sem o .0
    if (resultString.endsWith(".0")) return resultString.split(".").first()

    return resultString
}