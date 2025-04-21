package com.worldline.quiz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import quiz.composeapp.generated.resources.Res
import quiz.composeapp.generated.resources.test1


@Composable
@Preview
fun welcomeScreen(onStartButtonPushed: () -> Unit) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(10.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    /*Image(
                        painterResource(Res.drawable.test1),
                        contentDescription = "Welcome Image",
                        modifier = Modifier.fillMaxHeight()
                    )*/
                    Text(
                        text = "Bienvenue sur BetTracker !",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(all = 10.dp)
                    )
                    Text(
                        modifier = Modifier.padding(all = 10.dp),
                        text = "Suivez vos statistiques de paris et optimisez vos performances grâce à BetTracker.",
                    )
                    Button(
                        modifier = Modifier.padding(all = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFE10014), // Fond rouge
                            contentColor = Color.White // Texte blanc
                        ),
                        onClick = {
                            onStartButtonPushed()
                        }
                    ) {
                        Text("Connexion")
                    }
                }
            }
        }
    }
}