package com.example.elasclub.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProdutoDao {
    @Insert
    void inserir(Produto produto);

    @Query("SELECT * FROM produtos WHERE id = :id")
    Produto obterProduto(int id);

    @Query("SELECT * FROM produtos")
    List<Produto> getAllProducts();
}
