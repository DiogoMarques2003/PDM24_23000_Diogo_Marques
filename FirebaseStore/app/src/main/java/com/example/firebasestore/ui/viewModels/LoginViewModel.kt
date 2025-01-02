package com.example.firebasestore.ui.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.firebasestore.data.database.AppDatabase
import com.example.firebasestore.data.entity.User
import com.example.firebasestore.data.firebase.FirebaseAutentication
import com.example.firebasestore.data.firebase.FirebaseCollections
import com.example.firebasestore.data.firebase.FirebaseFirestore
import com.example.firebasestore.data.repository.UserRepository
import com.example.firebasestore.ui.navigation.NavigationPaths
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(database: AppDatabase, private val navController: NavController) :
    ViewModel() {
    private val userRepository = UserRepository(database.userDao())

    var isProcessing = MutableStateFlow(false)

    fun login(email: String, password: String, context: Context) {
        if (email.isEmpty() || !EMAIL_ADDRESS.matcher(email).matches()) {
            return Toast.makeText(
                context,
                "Email inválido",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (password.isEmpty() || password.length < 6) {
            return Toast.makeText(
                context,
                "Password inválida, tem de ter 6 caracteres",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModelScope.launch {
            try {
                isProcessing.value = true

                // Tentar fazer login
                val userId = FirebaseAutentication.loginAccount(email, password)
                    ?: return@launch Toast.makeText(
                        context,
                        "Não foi possível fazer login",
                        Toast.LENGTH_LONG
                    ).show()

                Log.w(TAG, "Aqui")

                // Obter o user da base de dados
                val firebaseUser = FirebaseFirestore.getData(FirebaseCollections.User, userId)
                    ?: return@launch Toast.makeText(
                        context,
                        "Não foi possível fazer login",
                        Toast.LENGTH_LONG
                    ).show()

                // Converter o user
                val user = firebaseUser.first()
                val userClass = User.firebaseMapToClass(user)

                // Inserir o user na base de dados local
                userRepository.insert(userClass)

                // Enviar para a tela de homepage
                navController.navigate(NavigationPaths.PRODUCT_LIST) {
                    popUpTo(0) { inclusive = true }
                }
            } finally {
                isProcessing.value = false
            }
        }
    }
}