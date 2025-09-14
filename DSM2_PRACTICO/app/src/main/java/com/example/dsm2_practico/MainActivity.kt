package com.example.dsm2_practico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnRegisterStudent: Button
    private lateinit var btnRegisterGrades: Button
    private lateinit var btnStudentList: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar vistas
        tvWelcome = findViewById(R.id.tvWelcome)
        tvUserEmail = findViewById(R.id.tvUserEmail)
        btnLogout = findViewById(R.id.btnLogout)
        btnRegisterStudent = findViewById(R.id.btnRegisterStudent)
        btnRegisterGrades = findViewById(R.id.btnRegisterGrades)
        btnStudentList = findViewById(R.id.btnStudentList)

        // Verificar si el usuario ha iniciado sesión
        val currentUser = auth.currentUser
        if (currentUser == null) {
            redirectToLoginActivity()
            return
        }

        // Mostrar información del usuario
        tvUserEmail.text = currentUser.email

        // Configurar listeners para los botones
        btnLogout.setOnClickListener {
            auth.signOut()
            redirectToLoginActivity()
        }

        btnRegisterStudent.setOnClickListener {
            val intent = Intent(this, RegisterStudentActivity::class.java)
            startActivity(intent)
        }

        btnRegisterGrades.setOnClickListener {
            val intent = Intent(this, NotaFinalActivity::class.java)
            startActivity(intent)
        }

        btnStudentList.setOnClickListener {
            // Aquí puedes abrir la actividad para ver la lista de alumnos
            Toast.makeText(this, "Funcionalidad de lista de alumnos", Toast.LENGTH_SHORT).show()
            // Intent para abrir StudentListActivity (deberás crearla)
            // val intent = Intent(this, StudentListActivity::class.java)
            // startActivity(intent)
        }
    }

    private fun redirectToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}