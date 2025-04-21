package com.worldline.quiz.data.models

import com.worldline.quiz.network.AuthClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session

    fun fetchUser(token: String) {
        launch {
            try {
                val user = AuthClient().fetchUser(token)
                if (user != null) {
                    _session.value = UserSession(user.id, token)
                    println("✅ Connecté : ${user.email}")
                } else {
                    println("⚠️ Utilisateur non trouvé")
                }
            } catch (e: Exception) {
                println("🔥 Erreur de connexion : ${e.message}")
            }
        }
    }

    fun logout() {
        _session.value = null
    }
}

data class UserSession(val userId: String, val token: String)
