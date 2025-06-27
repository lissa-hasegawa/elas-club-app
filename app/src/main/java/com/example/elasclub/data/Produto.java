package com.example.elasclub.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "produtos")
public class Produto {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nome;
    public String tamanho;
    public String cor;
    public String descricao;
    public byte[] foto;
}
