package com.example.registrocalificaciones

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdministradorDB(context: Context) : SQLiteOpenHelper (
    context,
    "registrosEstudiantes.db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE configuracion(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                carrera TEXT NOT NULL,
                grupo TEXT NOT NULL,
                notificaciones BOOLEAN NOT NULL
                )
            """.trimIndent()
        )
        db.execSQL(
            """
                 CREATE TABLE registroCalificaciones(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_estudiante INTEGER,
                asignatura TEXT NOT NULL,
                nota1 INT NOT NULL,
                nota2 INT NOT NULL,
                nota3 INT NOT NULL,
                nota4 INT NOT NULL,
                promedio INT NOT NULL,
                condicion TEXT NOT NULL,
                fecha DATE NOT NULL,
                FOREIGN KEY(id_estudiante) REFERENCES configuracion(id)
                )
            """.trimIndent()
        )
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        // Esto activa las llaves foráneas que definiste
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS configuracion")
        db.execSQL("DROP TABLE IF EXISTS registroCalificaciones")
        onCreate(db)
    }
}