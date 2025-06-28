package com.example.elasclub.ui.products;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elasclub.AdapterListView;
import com.example.elasclub.R;
import com.example.elasclub.data.AppDatabase;
import com.example.elasclub.data.Produto;
import com.example.elasclub.data.ProdutoDao;
import com.example.elasclub.databinding.FragmentProductsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;
    private ProdutoDao produtoDao;
    private AdapterListView produtoAdapter;
    private List<Produto> listaDeProdutos;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa o banco de dados e o DAO
        AppDatabase db = AppDatabase.getInstance(getContext());
        produtoDao = db.produtoDao();

        // Inicializa a lista de produtos e o adapter
        listaDeProdutos = new ArrayList<>();
        produtoAdapter = new AdapterListView(getContext(), listaDeProdutos);

        // Define o adapter na ListView
        binding.list.setAdapter(produtoAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Carrega os produtos sempre que o fragmento se torna visível
        carregarProdutos();
    }

    private void carregarProdutos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Obtém todos os produtos do banco de dados
                List<Produto> produtosDoBanco = produtoDao.getAllProducts();

                // Atualiza a UI na thread principal
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (produtosDoBanco != null && !produtosDoBanco.isEmpty()) {
                            // Atualiza o adapter com a nova lista de produtos
                            produtoAdapter.setProdutos(produtosDoBanco);
                            binding.textView3.setText(R.string.lista); // Altera o título se houver produtos
                        } else {
                            // Se não houver produtos, exibe uma mensagem
                            listaDeProdutos.clear();
                            produtoAdapter.notifyDataSetChanged();
                            binding.textView3.setText(R.string.nenhum); // Texto para "Nenhum produto cadastrado"
                        }
                    });
                }
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Erro ao carregar produtos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}