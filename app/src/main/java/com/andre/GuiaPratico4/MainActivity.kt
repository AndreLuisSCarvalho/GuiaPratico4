package com.andre.GuiaPratico4

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentServico = Intent(this, SincronizacaoService::class.java)

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {

                    ContextCompat.startForegroundService(this@MainActivity, intentServico)
                }) {
                    Text(text = "Iniciar Serviço")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    stopService(intentServico)
                }) {
                    Text(text = "Parar Serviço")
                }
            }
        }
    }
}
