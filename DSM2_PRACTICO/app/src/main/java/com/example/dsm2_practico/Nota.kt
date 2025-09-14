package com.example.dsm2_practico

class Nota {
    var estudianteId: String = ""
    var estudianteNombre: String = ""
    var grado: String = ""
    var materia: String = ""
    var notaFinal: Double = 0.0
    var key: String? = null

    // Constructor vacío requerido para Firebase
    constructor()

    constructor(estudianteId: String, estudianteNombre: String, grado: String, materia: String, notaFinal: Double) {
        this.estudianteId = estudianteId
        this.estudianteNombre = estudianteNombre
        this.grado = grado
        this.materia = materia
        this.notaFinal = notaFinal
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "estudianteId" to estudianteId,
            "estudianteNombre" to estudianteNombre,
            "grado" to grado,
            "materia" to materia,
            "notaFinal" to notaFinal,
            "key" to key
        )
    }
}