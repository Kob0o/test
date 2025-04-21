package com.worldline.quiz.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.worldline.quiz.data.dataclass.BetStatus
import com.worldline.quiz.data.models.BetViewModel

@Composable
fun HomeScreen(
    viewModel: BetViewModel,
    onAddBet: () -> Unit,
    onViewStats: () -> Unit,
    onBetSelected: (Int) -> Unit, // Nouveau callback pour accéder aux stats
    onLogout: () -> Unit // Nouveau callback
) {
    val bets by viewModel.bets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mes paris", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(bets) { bet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onBetSelected(bet.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Sport : ${bet.sport}")
                        Text("Événement : ${bet.event}")
                        Text("Cote : ${bet.cote}")
                        Text("Mise : ${bet.mise}€")
                        Text("Gain potentiel : ${bet.gainPotentiel}€")
                        Text("Statut : ${bet.statut}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Button(
                                onClick = {
                                    viewModel.updateBetStatus(bet.id, BetStatus.GAGNE)
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Gagné")
                            }
                            Button(
                                onClick = {
                                    viewModel.updateBetStatus(bet.id, BetStatus.PERDU)
                                }
                            ) {
                                Text("Perdu")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Bouton pour ajouter un pari
        Button(onClick = onAddBet, modifier = Modifier.fillMaxWidth()) {
            Text("Ajouter un pari")
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Bouton pour voir les statistiques
        Button(onClick = onViewStats, modifier = Modifier.fillMaxWidth()) {
            Text("Voir les statistiques")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.syncWithSupabase() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Chargement..." else "Recharger")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se déconnecter")
        }
    }
}
