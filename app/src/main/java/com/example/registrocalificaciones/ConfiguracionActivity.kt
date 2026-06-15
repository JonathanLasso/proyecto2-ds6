package com.example.registrocalificaciones

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.registrocalificaciones.databinding.ActivityConfiguracionBinding

class ConfiguracionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracionBinding
    private lateinit var preferencias: SharedPreferences

    // 1. REGISTRAR EL LANZADOR PARA PEDIR EL PERMISO DEL SISTEMA
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notificaciones activadas con éxito", Toast.LENGTH_SHORT).show()
        } else {
            // Si el usuario lo deniega, apagamos el switch visualmente para que no se confunda
            binding.switchNotificaciones.isChecked = false
            Toast.makeText(this, "Permiso denegado. No se enviarán alertas.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencias = getSharedPreferences("informacionEstudiante", Context.MODE_PRIVATE)

        guardarDatos()
        limpiarDatos()
        volverAlMenuConBoton()
        volverAlMenuConLaFlecha()
        mostrarInformacion() // <- Aquí ya cargamos todo, incluido el Switch
        configurarEscuchadorSwitch() // <- Aquí vigilamos si activan el Switch en tiempo real
    }

    // 2. DETECTAR CUANDO EL USUARIO MUEVE EL SWITCH
    private fun configurarEscuchadorSwitch() {
        binding.switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            // Si intenta encenderlo y estamos en Android 13 o superior...
            if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // ... y el permiso NO está concedido todavía
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // Pedimos el permiso en pantalla
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
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
                    putBoolean("notificaciones", notificaciones) // Guarda el estado del switch
                    apply()
                }
                Toast.makeText(applicationContext, "Datos guardados con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "No se permiten campos vacíos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 3. CARGAR EL ESTADO ANTERIOR DEL SWITCH
    private fun mostrarInformacion(){
        binding.etNombreCompleto.setText(preferencias.getString("nombreCompleto",""))
        binding.etCarrera.setText(preferencias.getString("carrera",""))
        binding.etGrupo.setText(preferencias.getString("grupo",""))

        // Leemos el valor guardado. Por defecto es 'true' para nuevos usuarios,
        // pero si ya se guardó antes, tomará el valor real (true o false).
        val estadoNotificaciones = preferencias.getBoolean("notificaciones", false)
        binding.switchNotificaciones.isChecked = estadoNotificaciones
    }

    private fun limpiarDatos(){
        binding.btnLimpiar.setOnClickListener {
            binding.etNombreCompleto.setText("")
            binding.etCarrera.setText("")
            binding.etGrupo.setText("")
            binding.switchNotificaciones.isChecked = false // Se cambia setChecked por .isChecked
            Toast.makeText(applicationContext, "Campos limpiados", Toast.LENGTH_SHORT).show()
        }
    }

    private fun volverAlMenuConLaFlecha(){
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun volverAlMenuConBoton(){
        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}