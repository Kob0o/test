package com.worldline.quiz.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Ajouter un pari", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = sport, onValueChange = { sport = it }, label = { Text("Sport") })
        OutlinedTextField(
            value = event,
            onValueChange = { event = it },
            label = { Text("Événement") })
        OutlinedTextField(value = cote, onValueChange = { cote = it }, label = { Text("Cote") })
        OutlinedTextField(value = mise, onValueChange = { mise = it }, label = { Text("Mise") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val newBet = Bet(
                id = generateRandomId(),
                sport = sport,
                event = event,
                cote = cote.toDoubleOrNull() ?: 0.0,
                mise = mise.toDoubleOrNull() ?: 0.0,
                gainPotentiel = (cote.toDoubleOrNull() ?: 0.0) * (mise.toDoubleOrNull() ?: 0.0),
                statut = BetStatus.EN_COURS,
                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                user_id = viewModel.userId // Inclure user_id depuis le ViewModel
            )
            viewModel.addBet(newBet)
            onBetAdded()
        }) {
            Text("Ajouter")
        }
    }
}
