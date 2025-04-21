package com.worldline.quiz.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.worldline.quiz.data.dataclass.Bet
import com.worldline.quiz.data.dataclass.BetStatus
import com.worldline.quiz.data.models.BetViewModel
import com.worldline.quiz.utils.generateRandomId
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun AddBetScreen(
    viewModel: BetViewModel,
    onBetAdded: () -> Unit
) {
    var sport by remember { mutableStateOf("") }
    var event by remember { mutableStateOf("") }
    var cote by remember { mutableStateOf("") }
    var mise by remember { mutableStateOf("") }

    StyledScaffold(title = "Ajouter un pari", showBack = true, onBack = onBetAdded) {
        OutlinedTextField(
            value = sport,
            onValueChange = { sport = it },
            label = { Text("Sport") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = event,
            onValueChange = { event = it },
            label = { Text("Événement") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = cote,
            onValueChange = { cote = it },
            label = { Text("Cote") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = mise,
            onValueChange = { mise = it },
            label = { Text("Mise") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val newBet = Bet(
                    id = generateRandomId(),
                    sport = sport,
                    event = event,
                    cote = cote.toDoubleOrNull() ?: 0.0,
                    mise = mise.toDoubleOrNull() ?: 0.0,
                    gainPotentiel = (cote.toDoubleOrNull() ?: 0.0) * (mise.toDoubleOrNull() ?: 0.0),
                    statut = BetStatus.EN_COURS,
                    date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    user_id = viewModel.userId
                )
                viewModel.addBet(newBet)
                onBetAdded()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE10014),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter le pari")
        }
    }
}


