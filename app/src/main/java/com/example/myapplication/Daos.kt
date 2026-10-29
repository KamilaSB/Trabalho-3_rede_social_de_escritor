package com.example.myapplication

import androidx.room3.*

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insert(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios")
    suspend fun buscarTodos(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun buscarPorId(id: Int): Usuario?

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)
}

@Dao
interface TextoDao {
    @Insert
    suspend fun insert(texto: Texto): Long

    @Query("SELECT * FROM textos ORDER BY dataCriacao DESC")
    suspend fun buscarTodos(): List<Texto>

    @Query("SELECT * FROM textos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Texto?

    @Query("SELECT * FROM textos WHERE autorId = :autorId ORDER BY dataCriacao DESC")
    suspend fun buscarPorAutor(autorId: Int): List<Texto>

    @Query("SELECT * FROM textos WHERE titulo LIKE '%' || :query || '%' OR categoria LIKE '%' || :query || '%' ORDER BY dataCriacao DESC")
    suspend fun pesquisar(query: String): List<Texto>

    @Update
    suspend fun update(texto: Texto)

    @Delete
    suspend fun delete(texto: Texto)
}
