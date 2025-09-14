package com.example.dsm2_practico

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.dsm2_practico.Nota
import com.example.dsm2_practico.Student

class NotaFinalActivity : AppCompatActivity() {

    private lateinit var spinnerEstudiantes: Spinner
    private lateinit var spinnerGrados: Spinner
    private lateinit var spinnerMaterias: Spinner
    private lateinit var etNotaFinal: TextInputEditText
    private lateinit var btnGuardarNota: Button
    private lateinit var btnCancelar: Button

    private lateinit var database: DatabaseReference
    private lateinit var estudiantesRef: DatabaseReference
    private lateinit var notasRef: DatabaseReference

    private var estudiantesList: MutableList<Student> = mutableListOf()
    private var estudiantesNombres: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota_final)

        // Inicializar vistas
        spinnerEstudiantes = findViewById(R.id.spinnerEstudiantes)
        spinnerGrados = findViewById(R.id.spinnerGrados)
        spinnerMaterias = findViewById(R.id.spinnerMaterias)
        etNotaFinal = findViewById(R.id.etNotaFinal)
        btnGuardarNota = findViewById(R.id.btnGuardarNota)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Inicializar referencias a la base de datos
        database = FirebaseDatabase.getInstance().reference
        estudiantesRef = database.child("estudiantes")
        notasRef = database.child("notas")

        // Configurar spinners con valores predefinidos
        configurarSpinners()

        // Cargar estudiantes desde Firebase
        cargarEstudiantes()

        // Configurar listeners
        btnGuardarNota.setOnClickListener {
            guardarNota()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun configurarSpinners() {
        // Configurar spinner de grados
        val gradosAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.grados,
            android.R.layout.simple_spinner_item
        )
        gradosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrados.adapter = gradosAdapter

        // Configurar spinner de materias
        val materiasAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.materias,
            android.R.layout.simple_spinner_item
        )
        materiasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMaterias.adapter = materiasAdapter
    }

    private fun cargarEstudiantes() {
        estudiantesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                estudiantesList.clear()
                estudiantesNombres.clear()

                for (data in snapshot.children) {
                    val estudiante = data.getValue(Student::class.java)
                    estudiante?.key = data.key
                    if (estudiante != null) {
                        estudiantesList.add(estudiante)
                        estudiantesNombres.add("${estudiante.nombre} - ${estudiante.grado}")
                    }
                }

                // Configurar el spinner con los nombres de estudiantes
                val adapter = ArrayAdapter(
                    this@NotaFinalActivity,
                    android.R.layout.simple_spinner_item,
                    estudiantesNombres
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerEstudiantes.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NotaFinalActivity, "Error al cargar estudiantes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guardarNota() {
        val selectedPosition = spinnerEstudiantes.selectedItemPosition
        if (selectedPosition == -1) {
            Toast.makeText(this, "Seleccione un estudiante", Toast.LENGTH_SHORT).show()
            return
        }

        val estudiante = estudiantesList[selectedPosition]
        val grado = spinnerGrados.selectedItem.toString()
        val materia = spinnerMaterias.selectedItem.toString()
        val notaFinalStr = etNotaFinal.text.toString().trim()

        // Validar campos
        if (notaFinalStr.isEmpty()) {
            Toast.makeText(this, "Ingrese la nota final", Toast.LENGTH_SHORT).show()
            return
        }

        val notaFinal = try {
            notaFinalStr.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "La nota debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar que la nota esté entre 0 y 10
        if (notaFinal < 0 || notaFinal > 10) {
            Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear objeto Nota
        val nota = Nota(
            estudiante.key ?: "",
            estudiante.nombre,
            grado,
            materia,
            notaFinal
        )

        // Guardar en Firebase
        val newKey = notasRef.push().key
        if (newKey != null) {
            nota.key = newKey
            notasRef.child(newKey).setValue(nota)
                .addOnSuccessListener {
                    Toast.makeText(this, "Nota registrada con éxito", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al registrar nota", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error al generar clave", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        etNotaFinal.text?.clear()
        if (spinnerEstudiantes.count > 0) {
            spinnerEstudiantes.setSelection(0)
        }
        spinnerGrados.setSelection(0)
        spinnerMaterias.setSelection(0)
    }
}