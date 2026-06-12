package com.example.registrocalificaciones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
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
            val editor = preferencias.edit()
            editor.putString("nombreCompleto", binding.etNombreCompleto.text.toString())
            editor.putString("carrera", binding.etCarrera.text.toString())
            editor.putString("grupo",binding.etGrupo.text.toString())
            editor.putBoolean("notificaciones", binding.switchNotificaciones.isChecked)
            editor.commit()
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