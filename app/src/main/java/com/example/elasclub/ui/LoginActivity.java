package com.example.elasclub.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elasclub.CadastroActivity;
import com.example.elasclub.R;
import android.widget.*;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elasclub.UserManager;
import com.example.elasclub.data.Usuario;
import com.example.elasclub.security.SecurityUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLoginEmail, edtLoginSenha;
    private Button btnLogar;
    private ImageButton btnVoltar;
    private UserManager userManager;
    private TextView tvLink;

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
        tvLink = findViewById(R.id.tvLink);


        btnVoltar.setOnClickListener(v -> finish());

        btnLogar.setOnClickListener(v -> {
            String emailDigitado = edtLoginEmail.getText().toString(); // Captura AGORA
            String senhaDigitada = edtLoginSenha.getText().toString(); // Captura AGORA

            // Chama o UserManager com email E senha
            // Você precisará mudar a assinatura de getUsuario no UserManager
            userManager.getUsuario(emailDigitado, new UserManager.UsuarioCallback() {
                @Override
                public void onUsuarioLoaded(Usuario user) { // O callback ainda recebe o objeto User
                    runOnUiThread(() -> { // Garante que o Toast roda na UI thread
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_LONG).show();
                            // TODO: Redirecionar para a tela principal
                        } else {
                            Toast.makeText(LoginActivity.this, "Email ou senha incorretos.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        });

        tvLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class); // Altere para sua Activity de Cadastro
            startActivity(intent);
        });
    }
}