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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var displayValue = remember { mutableStateOf("") }
    Box(modifier.background(Color.DarkGray)
                .fillMaxSize()
                .padding(top = 12.dp, start = 15.dp, end = 15.dp)) {
        Column (Modifier.fillMaxSize()
                        .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Text( text = displayValue.value,
                  Modifier.background(Color.White)
                          .fillMaxWidth()
                          .padding(vertical = 8.dp),
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
                createButton("7", Color.Gray) {}
                createButton("8", Color.Gray) {}
                createButton("9", Color.Gray) {}
                createButton("÷", Color.Black) {}
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("4", Color.Gray) {}
                createButton("5", Color.Gray) {}
                createButton("6", Color.Gray) {}
                createButton("X", Color.Black) {}
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("1", Color.Gray) {}
                createButton("2", Color.Gray) {}
                createButton("3", Color.Gray) {}
                createButton("-", Color.Black) {}
            }

            Row (horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                createButton("0", Color.Gray) {}
                createButton(".", Color.Gray) {}
                createButton("=", Color.Gray) {}
                createButton("+", Color.Black) {}
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