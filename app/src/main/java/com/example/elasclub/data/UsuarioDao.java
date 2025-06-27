package com.example.elasclub.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Insert
    void inserir(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario obterUsuario(int id);

    @Query("SELECT * FROM usuarios WHERE email = :email and senha = :senha LIMIT 1")
    Usuario getUserByLogin(String email, String senha);

    @Query("SELECT * FROM usuarios")
    List<Usuario> getAllUsers();
}
