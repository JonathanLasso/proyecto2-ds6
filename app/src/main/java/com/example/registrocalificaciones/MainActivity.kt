package com.example.registrocalificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.registrocalificaciones.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencias = getSharedPreferences("informacionEstudiante", Context.MODE_PRIVATE)
        mostrarInformacionEstudiante()
        configuracionPantallas()
        configuracionNotificaciones()
    }

    private fun configuracionPantallas(){
        binding.btnConfigurar.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroCalificacionActivity::class.java)
            startActivity(intent)
        }
        binding.btnHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
        }
        binding.btnSalir.setOnClickListener {
            finishAffinity()
        }
    }

    private fun configuracionNotificaciones(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_calificaciones",
                "Calificaciones",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun mostrarInformacionEstudiante(){
        binding.tvNombre.text = preferencias.getString("nombreCompleto","")
        binding.tvCarrera.text = preferencias.getString("carrera","")
    }
}