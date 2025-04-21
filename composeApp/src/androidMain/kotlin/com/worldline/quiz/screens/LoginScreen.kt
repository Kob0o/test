package com.worldline.quiz.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun LoginScreen() {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Connexion", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val authUrl =
                "https://kxtdhjouiuyjatcamfut.supabase.co/auth/v1/authorize?provider=google&redirect_to=myapp://callback"
            val intent = Intent(Intent.ACTION_VIEW, authUrl.toUri())
            context.startActivity(intent)
        }) {
            Text("Connexion avec Google")
        }
    }
}
