package com.example.elasclub;

import android.content.Context;

import com.example.elasclub.data.AppDatabase;
import com.example.elasclub.data.Usuario;
import com.example.elasclub.data.UsuarioDao;
import com.example.elasclub.security.SecurityUtils;

import java.util.List;

public class UserManager {
    private final UsuarioDao usuarioDao;

    public UserManager(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.usuarioDao = db.usuarioDao();
    }
    public void getUsuario(String email, UsuarioCallback callback) {
        new Thread(() -> {
            Usuario usuario = usuarioDao.getUserByEmail(email);
            callback.onUsuarioLoaded(usuario);
        }).start();
    }


    public interface UsuarioCallback {
        void onUsuarioLoaded(Usuario Usuario);
    }
}


