package com.example.elasclub;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {
    @Insert
    void inserir(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario obterUsuario(int id);
}
