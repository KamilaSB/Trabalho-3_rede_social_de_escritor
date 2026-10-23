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
<<<<<<< HEAD
    suspend fun atualizar(usuario: Usuario)
=======
    suspend fun update(usuario: Usuario)
>>>>>>> 91c0eaada25cbb11c5965bad087c776e3b495eab

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

<<<<<<< HEAD
    // Busca filtrada por autor e termo
    @Query("""
        SELECT * FROM textos 
        WHERE autorId = :autorId 
          AND (titulo LIKE '%' || :termo || '%' COLLATE NOCASE 
               OR conteudo LIKE '%' || :termo || '%' COLLATE NOCASE)
        ORDER BY dataCriacao DESC
    """)
    suspend fun buscarPorAutorETermo(autorId: Int, termo: String): List<Texto>

    // Busca GLOBAL por termo (para a tela de Explorar)
    @Query("""
        SELECT * FROM textos 
        WHERE (titulo LIKE '%' || :termo || '%' COLLATE NOCASE 
               OR conteudo LIKE '%' || :termo || '%' COLLATE NOCASE)
        ORDER BY dataCriacao DESC
    """)
    suspend fun buscarPorTermo(termo: String): List<Texto>

=======
>>>>>>> 91c0eaada25cbb11c5965bad087c776e3b495eab
    @Update
    suspend fun update(texto: Texto)

    @Delete
    suspend fun delete(texto: Texto)
}
