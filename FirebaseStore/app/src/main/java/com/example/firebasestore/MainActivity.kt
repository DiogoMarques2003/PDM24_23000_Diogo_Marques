package com.example.firebasestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.firebasestore.data.firebase.FirebaseAutentication
import com.example.firebasestore.data.firebase.FirebaseFirestore
import com.example.firebasestore.data.firebase.FirebaseStorage
import com.example.firebasestore.ui.navigation.Navigation
import com.example.firebasestore.ui.theme.FirebaseStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Start firebase
        FirebaseFirestore.start()
        FirebaseStorage.start()
        FirebaseAutentication.start()

        setContent {
            FirebaseStoreTheme {
                Navigation()
            }
        }
    }
}