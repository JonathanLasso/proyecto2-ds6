package com.example.registrocalificaciones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.registrocalificaciones.databinding.ActivityRegistroCalificacionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class RegistroCalificacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroCalificacionBinding
    private lateinit var preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroCalificacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencias = getSharedPreferences("informacionEstudiante", Context.MODE_PRIVATE)
        val idEstudiante = preferencias.getLong("id_estudiante", -1L).toInt()
        guardarCalificacion(idEstudiante)
        volverAlMenuConBoton()
        volverAlMenuConLaFlecha()
        limpiarDatos()
    }

    private fun guardarCalificacion(idEstudiante: Int){
        binding.btnGuardar.setOnClickListener {
            val asignatura = binding.etAsignatura.text.toString().trim()
            val nota1 = binding.etNota1.text.toString().toIntOrNull() ?: -1
            val nota2 = binding.etNota2.text.toString().toIntOrNull() ?: -1
            val nota3 = binding.etNota3.text.toString().toIntOrNull() ?: -1
            val nota4 = binding.etNota4.text.toString().toIntOrNull() ?: -1

            if(asignatura.isNotEmpty() && nota1 in 0..100 && nota2 in 0..100 && nota3 in 0..100 && nota4 in 0..100){
                val promedio = (nota1 + nota2 + nota3 + nota4) / 4
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaActual = sdf.format(Date())
                val condiciones = resources.getStringArray(R.array.condiciones)
                val estadoCondicion = when (promedio) {
                    in 91..100 -> condiciones[0]
                    in 81..90  -> condiciones[1]
                    in 71..80  -> condiciones[2]
                    in 61..70  -> condiciones[3]
                    else       -> condiciones[4]
                }
                binding.tvCondicion.text = estadoCondicion
                binding.tvPromedio.text = promedio.toString()
                val admin = AdministradorDB(this)
                val dbConsultar = admin.readableDatabase
                val cursor = dbConsultar.rawQuery(
                    "SELECT nombre, carrera, grupo FROM configuracion WHERE id = ?",
                    arrayOf(idEstudiante.toString())
                )
                if(cursor.moveToFirst()){
                    val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                    val carrera = cursor.getString(cursor.getColumnIndexOrThrow("carrera"))
                    val grupo = cursor.getString(cursor.getColumnIndexOrThrow("grupo"))
                }
                else {
                    Toast.makeText(applicationContext, "No se puedo cargar la información configurada.", Toast.LENGTH_SHORT).show()
                }
                val dbInsertar = admin.writableDatabase
                val datos = ContentValues().apply{
                    put("id_estudiante", idEstudiante)
                    put("asignatura", asignatura)
                    put("nota1", nota1)
                    put("nota2", nota2)
                    put("nota3", nota3)
                    put("nota4", nota4)
                    put("promedio",promedio.toString().toIntOrNull())
                    put("condicion", estadoCondicion)
                    put("fecha",fechaActual)
                }
                val resultado = dbInsertar.insert("registroCalificaciones",null,datos)
                cursor.close()
                dbConsultar.close()
                dbInsertar.close()
                // Intentar lanzar la notificación si corresponde
                Toast.makeText(this, "Calificaciones guardadas con éxito.", Toast.LENGTH_SHORT).show()
                enviarNotificacionCalificacion(asignatura, promedio.toString())
            } else {
                Toast.makeText(this, "No se permiten campos vacíos o notas inválidas.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enviarNotificacionCalificacion(asignatura: String, promedio: String) {
        val canalId = "canal_calificaciones"

        // Leemos el estado del switch (por defecto true si no se ha configurado)
        val activas = preferencias.getBoolean("notificaciones", true)

        if (activas) {
            val builder = NotificationCompat.Builder(this, canalId)
                .setSmallIcon(R.drawable.icono_notificacion)
                .setContentTitle("¡Calificación registrada!")
                .setContentText("Tu promedio en $asignatura fue de: $promedio")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            val manager = NotificationManagerCompat.from(this)

            // Únicamente validamos que el permiso ya esté concedido (sin solicitarlo en pantalla)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                manager.notify(1001, builder.build())
            }
        }
    }

    private fun limpiarDatos(){
        binding.btnLimpiar.setOnClickListener {
            binding.etAsignatura.setText("")
            binding.etNota1.setText("")
            binding.etNota2.setText("")
            binding.etNota3.setText("")
            binding.etNota4.setText("")
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