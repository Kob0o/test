package com.worldline.quiz.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun welcomeScreen(onStartButtonPushed: () -> Unit = {}) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenue sur BetTracker !",
                    fontSize = 28.sp,
                    color = Color(0xFFE10014)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Suivez vos statistiques de paris et optimisez vos performances.",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onStartButtonPushed() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFE10014),
                        contentColor = Color.White
                    )
                ) {
                    Text("Connexion")
                }
            }
        }
    }
}