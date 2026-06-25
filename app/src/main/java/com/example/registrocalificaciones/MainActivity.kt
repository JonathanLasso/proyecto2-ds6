package com.example.registrocalificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
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
        val idGuardado = preferencias.getLong("id_estudiante", -1L).toInt()
        mostrarInformacionEstudiante(idGuardado)
        configuracionPantallas()
        configuracionNotificaciones()
    }

    private fun configuracionPantallas(){
        binding.btnConfigurar.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroCalificacionActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)
            startActivity(intent)
            finish()
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

    private fun mostrarInformacionEstudiante(idEstudiante: Int){
        val admin = AdministradorDB(this)
        val db = admin.readableDatabase
        val cursor = db.rawQuery(
            "SELECT nombre, carrera, grupo, notificaciones FROM configuracion WHERE id = ?",
            arrayOf(idEstudiante.toString())
        )
        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val carrera = cursor.getString(cursor.getColumnIndexOrThrow("carrera"))
            val grupo = cursor.getString(cursor.getColumnIndexOrThrow("grupo"))
            val notificaciones = cursor.getInt(cursor.getColumnIndexOrThrow("notificaciones")) == 1

            binding.tvNombre.text = (nombre)
            binding.tvCarrera.text = (carrera)
        } else {
            Toast.makeText(applicationContext, "No se puedo cargar la información configurada.", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
    }
}