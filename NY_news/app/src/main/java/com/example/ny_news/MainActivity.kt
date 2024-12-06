package com.example.ny_news


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ny_news.ui.navigation.Navegation
import com.example.ny_news.ui.theme.NY_newsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NY_newsTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navegation(Modifier.padding(innerPadding))
                }
            }
        }
    }
}