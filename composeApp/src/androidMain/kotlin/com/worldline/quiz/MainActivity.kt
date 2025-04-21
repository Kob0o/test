package com.worldline.quiz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.worldline.quiz.data.models.BetViewModel
import com.worldline.quiz.data.models.LoginViewModel
import com.worldline.quiz.screens.LoginScreen

class MainActivity : ComponentActivity() {

    private val loginViewModel = LoginViewModel()
    private val betViewModel = BetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleAuthRedirect(intent)

        setContent {
            val session by loginViewModel.session.collectAsState()

            if (session == null) {
                // 👤 Pas connecté → écran de login (dispo uniquement en androidMain)
                LoginScreen()
            } else {
                // ✅ Connecté → passe viewModels à App
                betViewModel.setSession(session!!.token, session!!.userId)
                App(
                    betViewModel = betViewModel,
                    loginViewModel = loginViewModel
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleAuthRedirect(intent)
    }

    private fun handleAuthRedirect(intent: Intent?) {
        val uri: Uri = intent?.data ?: return
        val fragment = uri.fragment ?: return

        val accessToken = fragment.split("&")
            .find { it.startsWith("access_token=") }
            ?.substringAfter("=")

        if (accessToken != null) {
            loginViewModel.fetchUser(accessToken)
        }
    }
}
