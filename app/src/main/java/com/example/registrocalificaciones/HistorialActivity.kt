package com.example.registrocalificaciones

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.registrocalificaciones.databinding.ActivityHistorialBinding

class HistorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    // Ajustamos el nombre de tu variable al archivo real
    private val nombreArchivo = "historial_calificaciones.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Ejecutar la lectura e inflado dinámico del historial
        mostrarHistorial()
        // Configurar los botones de retroceso
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun mostrarHistorial() {
        val contenedor = binding.containerHistorial
        contenedor.removeAllViews()
        // 1. Verificamos si el archivo existe y tiene tamaño mayor a 0
        val archivo = getFileStreamPath(nombreArchivo)

        if (!archivo.exists() || archivo.length() == 0L) {
            // Si no existe o está vacío, mostramos el mensaje y salimos
            binding.txtMensajeVacio.visibility = View.VISIBLE
            return
        }
        // 2. Si hay datos, ocultamos el mensaje
        binding.txtMensajeVacio.visibility = View.GONE
        try {
            val lector = openFileInput(nombreArchivo).bufferedReader()
            val lineas = lector.readLines()
            lector.close()
            for (linea in lineas) {
                if (linea.isBlank()) continue
                val datos = linea.split(",") // Asignatura, Notas, Promedio, Condición, Fecha
                val vistaTarjeta = LayoutInflater.from(this).inflate(R.layout.tarjeta_nota_historial, contenedor, false)
                val txtAsignatura = vistaTarjeta.findViewById<android.widget.TextView>(R.id.txtAsignatura)
                val txtNotas = vistaTarjeta.findViewById<android.widget.TextView>(R.id.txtNotas)
                val txtPromedio = vistaTarjeta.findViewById<android.widget.TextView>(R.id.txtPromedio)
                val txtCondicion = vistaTarjeta.findViewById<android.widget.TextView>(R.id.txtCondicion)
                val txtFecha = vistaTarjeta.findViewById<android.widget.TextView>(R.id.txtFecha)
                val cardMateria = vistaTarjeta.findViewById<com.google.android.material.card.MaterialCardView>(R.id.cardMateria)
                // Asignar datos
                txtAsignatura.text = "Asignatura: ${datos[1]}"
                txtNotas.text = "Notas: ${datos[2].replace("-", ", ")}"
                txtPromedio.text = "Promedio: ${datos[3]}"
                txtCondicion.text = "Condición: ${datos[4]}"
                txtFecha.text = "Fecha: ${datos[5]}"
                // Colores dinámicos
                val (fondo, borde, texto) = when (datos[3].lowercase().trim()) {
                    "excelente" -> Triple("#E8F5E9", "#C8E6C9", "#1B5E20")
                    "bueno"     -> Triple("#E3EDFB", "#C5D7F2", "#1A237E")
                    else        -> Triple("#FFF8E1", "#FFE082", "#5D4037") // Regular
                }
                cardMateria.setCardBackgroundColor(Color.parseColor(fondo))
                cardMateria.setStrokeColor(ColorStateList.valueOf(Color.parseColor(borde)))
                txtAsignatura.setTextColor(Color.parseColor(texto))
                txtPromedio.setTextColor(Color.parseColor(texto))
                txtCondicion.setTextColor(Color.parseColor(texto))

                contenedor.addView(vistaTarjeta)
            }
        } catch (e: Exception) {
            binding.txtMensajeVacio.visibility = View.VISIBLE
        }
    }
}