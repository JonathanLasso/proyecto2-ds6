package com.example.registrocalificaciones

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.registrocalificaciones.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun configuracionPantallas(){
        binding.btnConfigurar.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
        }
        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroCalificacionActivity::class.java)
        }
        binding.btnHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
        }
        binding.btnSalir.setOnClickListener {
            finishAffinity()
        }
    }
}