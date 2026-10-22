package com.example.myapplication

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.ForeignKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val bio: String,
    val email: String
)

@Entity(
    tableName = "textos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["autorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Texto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val conteudo: String,
    val categoria: String,
    val autorId: Int,
    val dataCriacao: Long = System.currentTimeMillis()
)
