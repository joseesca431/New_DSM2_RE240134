package com.example.dsm2_practico

class Student {
    var nombre: String = ""
    var edad: Int = 0
    var grado: String = ""
    var direccion: String = ""
    var key: String? = null

    // Constructor vacío requerido para Firebase
    constructor()

    constructor(nombre: String, edad: Int, grado: String, direccion: String) {
        this.nombre = nombre
        this.edad = edad
        this.grado = grado
        this.direccion = direccion
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre,
            "edad" to edad,
            "grado" to grado,
            "direccion" to direccion,
            "key" to key
        )
    }
}