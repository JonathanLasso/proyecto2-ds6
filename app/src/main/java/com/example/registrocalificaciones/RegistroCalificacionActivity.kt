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
class RegistroCalificacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroCalificacionBinding
    private lateinit var preferencias: SharedPreferences
    private val archivo = "historial_calificaciones.txt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroCalificacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferencias = getSharedPreferences("informacionEstudiante", Context.MODE_PRIVATE)
        guardarCalificacion()
        volverAlMenuConBoton()
        volverAlMenuConLaFlecha()
        limpiarDatos()
    }

    private fun guardarCalificacion(){
        binding.btnGuardar.setOnClickListener {
            val nombre = preferencias.getString("nombreCompleto","")
            val grupo = preferencias.getString("grupo","")
            val asignatura = binding.etAsignatura.text.toString()
            val nota1 = binding.etNota1.text.toString().toIntOrNull() ?: -1
            val nota2 = binding.etNota2.text.toString().toIntOrNull() ?: -1
            val nota3 = binding.etNota3.text.toString().toIntOrNull() ?: -1
            val nota4 = binding.etNota4.text.toString().toIntOrNull() ?: -1

            if(asignatura.isNotEmpty() && nota1 in 0..100 && nota2 in 0..100 && nota3 in 0..100 && nota4 in 0..100){
                val promedio = (nota1 + nota2 + nota3 + nota4) / 4
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaActual = sdf.format(Date())
                val estadoCondicion = when (promedio) {
                    in 91..100 -> "Excelente"
                    in 81..90  -> "Bueno"
                    in 71..80  -> "Regular"
                    in 61..70  -> "Mínimo aprobado"
                    else       -> "Reprobado" // Captura cualquier nota de 60 hacia abajo automáticamente
                }
                binding.tvCondicion.text = estadoCondicion
                binding.tvPromedio.text = promedio.toString()
                val lineaDeDatos = "$asignatura,$grupo,$promedio,$estadoCondicion,$fechaActual\n"
                openFileOutput(archivo,MODE_APPEND).use { it.write(
                    lineaDeDatos.toByteArray()
                ) }
                Toast.makeText(this, "Datos guardados con éxito.", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "No se permiten campos vacíos.", Toast.LENGTH_SHORT).show()
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