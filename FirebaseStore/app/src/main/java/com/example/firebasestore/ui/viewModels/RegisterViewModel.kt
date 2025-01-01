package com.example.firebasestore.ui.viewModels

import android.content.Context
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

class RegisterViewModel(database: AppDatabase, private val navController: NavController) :
    ViewModel() {
    private val userRepository = UserRepository(database.userDao())

    var isProcessing = MutableStateFlow(false)

    fun register(name: String, email: String, password: String, context: Context) {
        if (name.isEmpty()) {
            return Toast.makeText(
                context,
                "Nome inválido",
                Toast.LENGTH_SHORT
            ).show()
        }

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

                // Criar conta do utilizador
                val userId = FirebaseAutentication.createAccount(email, password)
                    ?: return@launch Toast.makeText(
                        context,
                        "Erro ao criar a conta",
                        Toast.LENGTH_SHORT
                    ).show()

                val user = User(
                    id = userId,
                    email = email,
                    name = name
                )

                // Criar utilizador na base de dados
                FirebaseFirestore.insertData(FirebaseCollections.User, user.toFirebaseMap(), userId)
                    ?: return@launch Toast.makeText(
                        context,
                        "Erro ao criar a conta",
                        Toast.LENGTH_SHORT
                    ).show()

                // Inserir user na base de dados local
                userRepository.insert(user)

                // Redirecionar para a proxima página
                navController.navigate(NavigationPaths.ProductList)
            } finally {
                isProcessing.value = false
            }
        }
    }

}