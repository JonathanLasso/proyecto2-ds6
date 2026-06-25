package com.example.registrocalificaciones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.registrocalificaciones.databinding.ActivityHistorialBinding
import com.google.android.material.card.MaterialCardView

class HistorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    private lateinit var preferencias: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Ejecutar la lectura e inflado dinámico del historial
        preferencias = getSharedPreferences("informacionEstudiante", Context.MODE_PRIVATE)
        val idEstudiante = preferencias.getLong("id_estudiante", -1L).toInt()
        mostrarHistorial(idEstudiante)
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
    private fun mostrarHistorial(idEstudiante: Int) {
        val contenedor = binding.containerHistorial
        contenedor.removeAllViews()

        val admin = AdministradorDB(this)
        val db = admin.readableDatabase

        // Consultamos solo las calificaciones que pertenecen al estudiante seleccionado
        val cursor = db.rawQuery(
            "SELECT * FROM registroCalificaciones WHERE id_estudiante = ?",
            arrayOf(idEstudiante.toString())
        )

        if (cursor.count == 0) {
            binding.txtMensajeVacio.visibility = View.VISIBLE
        } else {
            binding.txtMensajeVacio.visibility = View.GONE

            while (cursor.moveToNext()) {
                // Extraer datos usando el nombre de las columnas (más seguro)
                val asignatura = cursor.getString(cursor.getColumnIndexOrThrow("asignatura"))
                val n1 = cursor.getInt(cursor.getColumnIndexOrThrow("nota1"))
                val n2 = cursor.getInt(cursor.getColumnIndexOrThrow("nota2"))
                val n3 = cursor.getInt(cursor.getColumnIndexOrThrow("nota3"))
                val n4 = cursor.getInt(cursor.getColumnIndexOrThrow("nota4"))
                val promedio = cursor.getInt(cursor.getColumnIndexOrThrow("promedio"))
                val condicion = cursor.getString(cursor.getColumnIndexOrThrow("condicion"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))

                // Inflar vista
                val vistaTarjeta = LayoutInflater.from(this).inflate(R.layout.tarjeta_nota_historial, contenedor, false)

                // Asignar datos a la vista
                vistaTarjeta.findViewById<TextView>(R.id.txtAsignatura).text = "Asignatura: $asignatura"
                vistaTarjeta.findViewById<TextView>(R.id.txtNotas).text = "Notas: $n1, $n2, $n3, $n4"
                vistaTarjeta.findViewById<TextView>(R.id.txtPromedio).text = "Promedio: $promedio"
                vistaTarjeta.findViewById<TextView>(R.id.txtCondicionHistorial).text = "Condición: $condicion"
                vistaTarjeta.findViewById<TextView>(R.id.txtFecha).text = "Fecha: $fecha"

                val cardMateria = vistaTarjeta.findViewById<MaterialCardView>(R.id.cardMateria)

                // Colores dinámicos
                val (fondo, borde, texto) = when (condicion.lowercase().trim()) {
                    "excelente" -> Triple("#E8F5E9", "#C8E6C9", "#1B5E20")
                    "bueno"     -> Triple("#E8F8F5", "#A3E4D7", "#117A65")
                    "regular"   -> Triple("#FFFDE7", "#FFF59D", "#F57F17")
                    "mínimo aprobado", "minimo aprobado" -> Triple("#FFF3E0", "#FFCC80", "#E65100")
                    else        -> Triple("#FFEBEE", "#FFCDD2", "#C62828")
                }
                cardMateria.setCardBackgroundColor(Color.parseColor(fondo))
                cardMateria.setStrokeColor(ColorStateList.valueOf(Color.parseColor(borde)))
                contenedor.addView(vistaTarjeta)
            }
        }

        cursor.close()
        db.close()
    }
}