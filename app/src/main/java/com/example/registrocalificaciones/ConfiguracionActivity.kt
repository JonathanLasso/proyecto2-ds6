package com.example.registrocalificaciones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.registrocalificaciones.databinding.ActivityConfiguracionBinding

class ConfiguracionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracionBinding
    private lateinit var preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencias = getSharedPreferences("informacionEstudiante", Context.MODE_PRIVATE)
        guardarDatos()
        limpiarDatos()
        volverAlMenuConBoton()
        volverAlMenuConLaFlecha()
        mostrarInformacion()
    }

    private fun guardarDatos(){
        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombreCompleto.text.toString().trim()
            val carrera = binding.etCarrera.text.toString().trim()
            val grupo = binding.etGrupo.text.toString().trim()
            val notificaciones = binding.switchNotificaciones.isChecked

            if (nombre.isNotEmpty() && carrera.isNotEmpty() && grupo.isNotEmpty()) {
                preferencias.edit().apply {
                    putString("nombreCompleto", nombre)
                    putString("carrera", carrera)
                    putString("grupo", grupo)
                    putBoolean("notificaciones", notificaciones)
                    apply()
                }
                Toast.makeText(applicationContext, "Datos guardados con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "No se permiten campos vacíos.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun mostrarInformacion(){
        binding.etNombreCompleto.setText(preferencias.getString("nombreCompleto",""))
        binding.etCarrera.setText(preferencias.getString("carrera",""))
        binding.etGrupo.setText(preferencias.getString("grupo",""))
    }

    private fun limpiarDatos(){
        binding.btnLimpiar.setOnClickListener {
            binding.etNombreCompleto.setText("")
            binding.etCarrera.setText("")
            binding.etGrupo.setText("")
            binding.switchNotificaciones.setChecked(false)
        }
    }

    private fun volverAlMenuConLaFlecha(){
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun volverAlMenuConBoton(){
        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}