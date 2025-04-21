package com.worldline.quiz.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.worldline.quiz.data.dataclass.BetStatus
import com.worldline.quiz.data.models.BetViewModel

@Composable
fun HomeScreen(
    viewModel: BetViewModel,
    onAddBet: () -> Unit,
    onViewStats: () -> Unit,
    onBetSelected: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val bets by viewModel.bets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LazyScaffold(title = "Mes paris") {
        items(bets) { bet ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 6.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .clickable { onBetSelected(bet.id) }
                        .padding(16.dp)
                ) {
                    Text("Sport : ${bet.sport}", style = MaterialTheme.typography.subtitle1)
                    Text("Événement : ${bet.event}")
                    Text("Cote : ${bet.cote}")
                    Text("Mise : ${bet.mise}€")
                    Text("Gain potentiel : ${bet.gainPotentiel}€")
                    Text("Statut : ${bet.statut}")
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Button(
                            onClick = { viewModel.updateBetStatus(bet.id, BetStatus.GAGNE) },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFE10014),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Gagné")
                        }
                        Button(
                            onClick = { viewModel.updateBetStatus(bet.id, BetStatus.PERDU) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFE10014),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Perdu")
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            @Composable
            fun redButton(text: String, onClick: () -> Unit) {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFE10014),
                        contentColor = Color.White
                    )
                ) {
                    Text(text)
                }
            }

            redButton("Ajouter un pari", onAddBet)
            redButton("Voir les statistiques", onViewStats)
            redButton(if (isLoading) "Chargement..." else "Recharger") {
                if (!isLoading) viewModel.syncWithSupabase()
            }
            redButton("Se déconnecter", onLogout)
        }
    }
}
