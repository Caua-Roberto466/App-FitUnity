package com.example.fitunity.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinoDetalheScreen(treino: Treino) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(treino.titulo) }) }
    ) { padding ->
        Text(
            text = "Detalhes de ${treino.titulo}: ${treino.duracaoMin} minutos, nível ${treino.nivel.label}.",
            modifier = Modifier.padding(padding)
        )
    }
}