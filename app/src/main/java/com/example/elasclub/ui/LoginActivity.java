package com.example.elasclub.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.elasclub.R;
import android.widget.*;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elasclub.UserManager;
import com.example.elasclub.security.SecurityUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLoginEmail, edtLoginSenha;
    private Button btnLogar;
    private ImageButton btnVoltar;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userManager = new UserManager(this);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginSenha = findViewById(R.id.edtLoginSenha);
        btnLogar = findViewById(R.id.btnLogar);
        btnVoltar = findViewById(R.id.btnVoltarLogin);

        String email = edtLoginEmail.getText().toString();
        String senha = SecurityUtils.hashPassword(edtLoginEmail.getText().toString());

        btnVoltar.setOnClickListener(v -> finish());

        btnLogar.setOnClickListener(v -> {
            userManager.getUsuario(email, user -> runOnUiThread(() -> {
                if (user != null) {
                    Toast.makeText(this, "Deu certo!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_LONG).show();
                }
            }));
        });
    }
}