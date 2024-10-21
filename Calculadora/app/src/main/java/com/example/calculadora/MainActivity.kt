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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.example.calculadora.ui.CreateButton
import com.example.calculadora.ui.theme.CalculadoraTheme
import kotlin.math.sqrt

//Tipos
data class Button(
    val name : String,
    val color: Color,
    val funcName : String = "",
    val parameter: String = ""
)

// Variaveis globais
var calcArray: Array<String> = emptyArray()
var memoryValue = "0"
var equalsCalc = false
var resetMemory = false

// Constantes
val ButtonLayout: Array<Array<Button>> = arrayOf(
    arrayOf(Button("MRC", Color.Black, "getMemory"),
        Button("M-", Color.Black, "calcToMemory", "-"),
        Button("M+", Color.Black, "calcToMemory", "+"),
        Button("ON/C", Color.Red, "clear")),
    arrayOf(Button("√", Color.Black, "squareRoot"),
        Button("%", Color.Black, "addSymbol", "%"),
        Button("+/-", Color.Black, "toggleSign"),
        Button("CE", Color.Red, "clearInfos")),
    arrayOf(Button("7", Color.Gray, "addNumber"),
        Button("8", Color.Gray, "addNumber"),
        Button("9", Color.Gray, "addNumber"),
        Button("÷", Color.Black, "addSymbol", "/")),
    arrayOf(Button("4", Color.Gray, "addNumber"),
        Button("5", Color.Gray, "addNumber"),
        Button("6", Color.Gray, "addNumber"),
        Button("X", Color.Black, "addSymbol", "*")),
    arrayOf(Button("1", Color.Gray, "addNumber"),
        Button("2", Color.Gray, "addNumber"),
        Button("3", Color.Gray, "addNumber"),
        Button("-", Color.Black, "addSymbol", "-")),
    arrayOf(Button("0", Color.Gray, "addNumber"),
        Button(".", Color.Gray, "addDot"),
        Button("=", Color.Gray, "calcResult"),
        Button("+", Color.Black, "addSymbol", "+"))
)
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
                          .height(80.dp)
                          .wrapContentHeight(Alignment.Bottom),
                  textAlign = TextAlign.End,
                  fontSize = 30.sp)

            for (buttons in ButtonLayout) {
                Row ( Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    for (button in buttons) {
                        CreateButton(button.name, button.color) {
                            displayValue.value = when (button.funcName) {
                                "getMemory" -> getMemory(displayValue.value)
                                "calcToMemory" -> calcToMemory(displayValue.value, button.parameter)
                                "squareRoot" -> squareRoot()
                                "addSymbol" -> addSymbol(button.parameter, displayValue.value)
                                "toggleSign" -> toggleSign()
                                "clearInfos" -> clearInfos()
                                "addNumber" -> addNumber(button.name)
                                "addDot" -> addDot()
                                "calcResult" -> calcResult()
                                "clear" -> clear()
                                else -> displayValue.value
                            }
                        }
                    }
                }
            }
        }
    }
}

fun clearInfos(): String {
    // Colocar o reset da memoria a falso
    resetMemory = false
    equalsCalc = false

    calcArray = emptyArray()
    return ""
}

fun addNumber(number: String): String {
    // Colocar o reset da memoria a falso
    resetMemory = false

    // Se clicar num nº ao final de clicar no igual recomeça
    if (equalsCalc && calcArray.size == 1) {
        calcArray = arrayOf()
        equalsCalc = false
    }

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

    // Validar se a string é apenas um 0, se for remover o 0
    if (lastString == "0") lastString = ""

    // Adicionar o novo nº a ultima string
    lastString += number
    calcArray[calcArray.size - 1] = lastString
    return lastString
}

fun addSymbol(symbol: String, lastValue: String): String {
    // Colocar o reset da memoria a falso
    resetMemory = false

    // Se o array tiver vazio ou tiver dado um erro adicionar um 0 e operador
    if (calcArray.isEmpty() || calcArray.first() == "ERR") {
        calcArray = arrayOf("0", symbol)
        return "0"
    }

    // Se o array já tiver 3 possições fazer o calculo e colocar o resultado e simbolo no array
    if (calcArray.size == 3) {
        var result = calcResult()
        calcArray = arrayOf(result, symbol)
        //Setar a flag de calculo a false
        equalsCalc = false
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
    // Colocar o reset da memoria a falso
    resetMemory = false

    // Se clicar no "." ao final de clicar no igual recomeça
    if (equalsCalc && calcArray.size == 1) {
        calcArray = arrayOf()
        equalsCalc = false
    }

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

fun squareRoot(): String {
    // Colocar o reset da memoria a falso
    resetMemory = false

    var numValue: Double

    // Se não tiver nenhum item no array ou tiver dado erro adicionar o simbolo de negativo
    if (calcArray.isEmpty() || calcArray.first() == "ERR") {
        calcArray = arrayOf("ERR")
        return "ERR"
    }

    // fazer a raiz do ultimo elemento
    if (calcArray.size == 3) {
        numValue = calcArray.last().toDouble()
        numValue = sqrt(numValue)
        calcArray[calcArray.size - 1] = removeZeros(numValue)
        return calcArray.last()
    }

    // Se não fazer a raiz do 1º elemento
    numValue = calcArray.first().toDouble()
    numValue = sqrt(numValue)
    calcArray[0] = removeZeros(numValue)
    return calcArray.first()
}

fun toggleSign(): String {
    // Colocar o reset da memoria a falso
    resetMemory = false

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
    // Colocar o reset da memoria a falso
    resetMemory = false

    //Setar a flag de calculo a true
    equalsCalc = true

    var firstNumber: Double
    var operationValue: String

    // Se o array estiver vazio não devolver nada
    if (calcArray.isEmpty()) return ""

    // Se o tamanho for 1 devolver o valor do array
    if (calcArray.size == 1) {
        return calcArray.first()
    }

    // Se tiver dois elementos fazer a operação com o 1º e o operador
    if (calcArray.size == 2) {
        firstNumber = calcArray.first().toDouble()
        operationValue = operatorResolve(firstNumber, calcArray[1], firstNumber)
        // Colocar o resultado no array
        calcArray = arrayOf(operationValue)
        return operationValue
    }

    // Fazer o calculo de quanto tem as 3 possições
    firstNumber = calcArray.first().toDouble()
    var secondNumber = calcArray.last().toDouble()
    operationValue = operatorResolve(firstNumber, calcArray[1], secondNumber)
    // Colocar o resultado no array
    calcArray = arrayOf(operationValue)
    return operationValue
}

fun operatorResolve(firstNumber: Double, operator: String, secondNumber: Double): String {
    if (operator == "/" && secondNumber == 0.0) {
        return "ERR"
    }

    var result = when(operator) {
        "+" -> firstNumber + secondNumber
        "-" -> firstNumber - secondNumber
        "*" -> firstNumber * secondNumber
        "/" -> firstNumber / secondNumber
        "%" -> firstNumber % secondNumber
        else -> 0
    }

    return removeZeros(result)
}

fun removeZeros(number: Number): String {
    var resultString = number.toString()
    // Se acabar por .0 devolver sem o .0
    if (resultString.endsWith(".0")) return resultString.split(".").first()

    return resultString
}

fun calcToMemory(displayValue: String, symbol: String): String {
    // Colocar o reset da memoria a falso
    resetMemory = false

    // Se no display tiver o erro parar o processamento
    if (displayValue == "ERR") return displayValue

    // adicionar o valor a memoria
    var numMemoryValue = displayValue.toDouble()
    memoryValue = operatorResolve(memoryValue.toDouble(), symbol, numMemoryValue)

    return displayValue
}

fun getMemory(displayValue: String): String {
    // Se for a 1º vez a clicar no botão devolver o valor
    if (!resetMemory) {
        resetMemory = true

        // Alterar o valor no array de calculo também
        when (calcArray.size) {
            1 -> calcArray[0] = memoryValue
            2 -> calcArray += memoryValue
            else -> calcArray[2] = memoryValue
        }

        return  memoryValue
    }

    // Se for a 2º vez seguida apagar o valor
    resetMemory = false
    memoryValue = "0"
    return displayValue
}

fun clear(): String {
    // Colocar o reset da memoria a falso
    resetMemory = false

    if (calcArray.isEmpty()) return ""

    if (calcArray.size == 3) {
        calcArray[calcArray.size - 1] = "0"
        return "0"
    }

    equalsCalc = false
    calcArray = arrayOf("0")
    return "0"
}
