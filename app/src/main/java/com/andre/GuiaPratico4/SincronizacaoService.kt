package com.andre.GuiaPratico4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SincronizacaoService : Service() {

    companion object {
        const val CHANNEL_ID = "canal_sincronizacao_ativa"
        const val NOTIFICATION_ID = 1001
    }

    // Escopo dedicado de Corrotinas em thread de I/O (nunca bloqueia a Main Thread)
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        criarCanalDeNotificacao()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 1. Constrói a notificação obrigatória para Serviços em Primeiro Plano
        val notificacao = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sincronização de Alta Performance")
            .setContentText("Coletando telemetria em segundo plano...")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setOngoing(true) // Impede que o usuário deslize e apague enquanto ativo
            .build()

        // 2. Promove o serviço para Foreground nos primeiros 5 segundos (obrigatório no Android)
        startForeground(NOTIFICATION_ID, notificacao)

        // 3. Inicia o trabalho assíncrono em background
        serviceScope.launch {
            var ciclo = 1
            while (isActive) {
                Log.i("SERVICE_CUSTOM", "Executando ciclo de sincronização #$ciclo na thread: ${Thread.currentThread().name}")
                ciclo++
                delay(3000)
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.i("SERVICE_CUSTOM", "Serviço encerrado (onDestroy)")
    }

    private fun criarCanalDeNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Canal de Sincronização Ativa",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }
}
