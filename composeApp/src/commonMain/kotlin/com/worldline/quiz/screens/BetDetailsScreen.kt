package com.worldline.quiz.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.worldline.quiz.data.dataclass.BetStatus
import com.worldline.quiz.data.models.BetViewModel

@Composable
fun BetDetailsScreen(
    betId: Int,
    viewModel: BetViewModel,
    onBack: () -> Unit
) {
    val bet = viewModel.bets.collectAsState().value.find { it.id == betId }

    if (bet == null) {
        Text("Pari introuvable")
        return
    }

    var mise by remember { mutableStateOf(bet.mise.toString()) }
    var cote by remember { mutableStateOf(bet.cote.toString()) }
    var statut by remember { mutableStateOf(bet.statut) }

    StyledScaffold(title = "Modifier le pari", showBack = true, onBack = onBack) {
        Text("Sport : ${bet.sport}", style = MaterialTheme.typography.body1)
        Text("Événement : ${bet.event}", style = MaterialTheme.typography.body1)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = mise,
            onValueChange = { mise = it },
            label = { Text("Mise (€)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = cote,
            onValueChange = { cote = it },
            label = { Text("Cote") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Statut")
        Row {
            BetStatus.values().forEach { status ->
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    RadioButton(selected = statut == status, onClick = { statut = status })
                    Text(status.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateBet(
                    id = bet.id,
                    mise = mise.toDoubleOrNull() ?: bet.mise,
                    cote = cote.toDoubleOrNull() ?: bet.cote,
                    statut = statut
                )
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE10014),
                contentColor = Color.White
            )
        ) {
            Text("Enregistrer")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.deleteBet(bet.id)
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error,
                contentColor = MaterialTheme.colors.onError
            )
        ) {
            Text("Supprimer")
        }
    }
}


