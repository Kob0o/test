package com.worldline.quiz

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.worldline.quiz.data.models.BetViewModel
import com.worldline.quiz.data.models.LoginViewModel
import com.worldline.quiz.screens.DesktopLoginScreen

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
                session?.let { betViewModel.setSession(it.token, session!!.userId) }
            }
        } else {
            App(
                betViewModel = betViewModel,
                loginViewModel = loginViewModel
            )
        }
    }
}
