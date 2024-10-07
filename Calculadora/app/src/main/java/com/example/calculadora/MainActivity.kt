package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadora.ui.theme.CalculadoraTheme

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
    var calcValue = "1234"

    Column (Modifier.background(Color.DarkGray)
                    .fillMaxSize()) {
        Row (
            Modifier.fillMaxWidth()
        ) {
            Text( text = calcValue,
                Modifier.background(Color.White).fillMaxWidth(),
                style = LocalTextStyle.current.merge(
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                ))
        }

        Row {
            Button(onClick = {}) { Text("7") }
            Button(onClick = {}) { Text("8") }
            Button(onClick = {}) { Text("9") }
            Button(onClick = {}) { Text("%") }
        }

        Row {
            Button(onClick = {}) { Text("7") }
            Button(onClick = {}) { Text("8") }
            Button(onClick = {}) { Text("9") }
            Button(onClick = {}) { Text("%") }
        }

        Row {
            Button(onClick = {}) { Text("7") }
            Button(onClick = {}) { Text("8") }
            Button(onClick = {}) { Text("9") }
            Button(onClick = {}) { Text("%") }
        }

        Row {
            Button(onClick = {}) { Text("7") }
            Button(onClick = {}) { Text("8") }
            Button(onClick = {}) { Text("9") }
            Button(onClick = {}) { Text("%") }
        }
    }
}