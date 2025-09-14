package com.example.dsm2_practico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterStudentActivity : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etEdad: TextInputEditText
    private lateinit var etGrado: TextInputEditText
    private lateinit var etDireccion: TextInputEditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_student)

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre)
        etEdad = findViewById(R.id.etEdad)
        etGrado = findViewById(R.id.etGrado)
        etDireccion = findViewById(R.id.etDireccion)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Inicializar referencia a la base de datos
        database = FirebaseDatabase.getInstance().getReference("estudiantes")

        // Configurar listeners
        btnGuardar.setOnClickListener {
            guardarEstudiante()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarEstudiante() {
        val nombre = etNombre.text.toString().trim()
        val edadStr = etEdad.text.toString().trim()
        val grado = etGrado.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()

        // Validar campos
        if (nombre.isEmpty() || edadStr.isEmpty() || grado.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = try {
            edadStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear objeto Student
        val estudiante = Student(nombre, edad, grado, direccion)

        // Guardar en Firebase
        val newKey = database.push().key
        if (newKey != null) {
            estudiante.key = newKey
            database.child(newKey).setValue(estudiante)
                .addOnSuccessListener {
                    Toast.makeText(this, "Estudiante registrado con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al registrar estudiante", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error al generar clave", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        etNombre.text?.clear()
        etEdad.text?.clear()
        etGrado.text?.clear()
        etDireccion.text?.clear()
    }
}