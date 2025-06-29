package com.example.elasclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.elasclub.databinding.ActivityHomeBinding;
import com.example.elasclub.databinding.ActivityMainBinding;
import com.example.elasclub.ui.products.ProductsFragment;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Button btnCadastrarProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Inicializar o botão de Cadastro de Produto
        btnCadastrarProduto = findViewById(R.id.btnCadastrarProduto);
        btnCadastrarProduto.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CadastroProdutoActivity.class);
            startActivity(intent);
        });

        if (savedInstanceState == null) {
            loadProductsFragment();
        }
    }

    private void loadProductsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.products_fragment_container, new ProductsFragment());

        // Opcional: Adicionar ao back stack se você quiser que o botão voltar remova o fragmento
        // Se este é o único fragmento e a Activity deve fechar, não adicione ao back stack.
        // fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}