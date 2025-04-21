package com.worldline.quiz

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.worldline.quiz.data.models.BetViewModel
import com.worldline.quiz.data.models.LoginViewModel
import com.worldline.quiz.screens.DesktopLoginScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun main() = application {
    val loginViewModel = remember { LoginViewModel() }
    val betViewModel = remember { BetViewModel() }
    val session by loginViewModel.session.collectAsState()

    Window(
        onCloseRequest = ::exitApplication,
        title = "BetTracker Desktop"
    ) {
        if (session == null) {
            DesktopLoginScreen(loginViewModel = loginViewModel) {
                CoroutineScope(Dispatchers.Main).launch {
                    loginViewModel.session.collectLatest { session ->
                        if (session != null) {
                            betViewModel.setSession(session.token, session.userId)
                        }
                    }
                }
            }

        } else {
            App(
                betViewModel = betViewModel,
                loginViewModel = loginViewModel
            )
        }
    }
}
