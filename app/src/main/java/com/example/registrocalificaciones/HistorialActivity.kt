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
            finish()
        }
        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
                val txtCondicion = vistaTarjeta.findViewById<android.widget.TextView>(R.id.txtCondicionHistorial)
                val txtFecha = vistaTarjeta.findViewById<android.widget.TextView>(R.id.txtFecha)
                val cardMateria = vistaTarjeta.findViewById<com.google.android.material.card.MaterialCardView>(R.id.cardMateria)
                // Asignar datos
                txtAsignatura.text = "Asignatura: ${datos[1]}"
                // Concatenamos las 4 notas individuales (índices 3, 4, 5 y 6)
                txtNotas.text = "Notas: ${datos[3]}, ${datos[4]}, ${datos[5]}, ${datos[6]}"
                txtPromedio.text = "Promedio: ${datos[7]}"
                txtCondicion.text = "Condición: ${datos[8]}"
                txtFecha.text = "Fecha: ${datos[9]}"
                // Colores dinámicos basados en la condición (índice 8)
                val condicionLimpia = datos[8].lowercase().trim()
                // Triple(Color de Fondo, Color de Borde, Color de Texto)
                val (fondo, borde, texto) = when (condicionLimpia) {
                    "excelente"       -> Triple("#E8F5E9", "#C8E6C9", "#1B5E20") // Verde Oscuro
                    "bueno"           -> Triple("#E8F8F5", "#A3E4D7", "#117A65") // Verde Normal
                    "regular"         -> Triple("#FFFDE7", "#FFF59D", "#F57F17") // Amarillo / Amarillo Oscuro
                    "mínimo aprobado",
                    "minimo aprobado" -> Triple("#FFF3E0", "#FFCC80", "#E65100") // Naranja
                    "reprobado"       -> Triple("#FFEBEE", "#FFCDD2", "#C62828") // Rojo
                    else              -> Triple("#F5F5F5", "#E0E0E0", "#424242") // Gris por si acaso
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