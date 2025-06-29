package com.example.elasclub.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nome;
    public String email;
    public String senha;
    public String hashedPassword;
    public byte[] foto;

    public String getHashedPassword() {
        return hashedPassword;
    }
}