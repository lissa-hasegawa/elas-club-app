package com.example.elasclub.ui.actions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elasclub.R;
import com.example.elasclub.data.AppDatabase;
import com.example.elasclub.data.Produto;
import com.example.elasclub.data.ProdutoDao;
import com.example.elasclub.databinding.FragmentCadastroProdutoBinding;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executors;

public class CadastroProdutoFragment extends Fragment {

    private FragmentCadastroProdutoBinding binding;
    private Bitmap imgProduto;
    ActivityResultLauncher<Intent> takePictureLauncher;
    private ProdutoDao produtoDao;

    public CadastroProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Verifica se a operação da câmera foi bem-sucedida
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            // Obtém o bitmap da imagem capturada
                            imgProduto = (Bitmap) data.getExtras().get("data");
                            // Define a imagem capturada na ImageView
                            binding.imgViewFoto.setImageBitmap(imgProduto);
                        }
                    } else {
                        // Mensagem caso a captura da imagem seja cancelada ou falhe
                        Toast.makeText(getContext(), "Captura de imagem cancelada.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCadastroProdutoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imgViewFoto.setOnClickListener(v -> {
            // Cria um Intent para abrir a câmera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Inicia a atividade da câmera usando o launcher
            takePictureLauncher.launch(cameraIntent);
        });

        binding.btnNovoProduto.setOnClickListener(v -> {
            // Obtém os dados dos campos de texto
            String nomeProduto = binding.edtProduto.getText().toString().trim();
            String tamanho = binding.edtTamanho.getText().toString().trim();
            String cor = binding.edtCor.getText().toString().trim();
            String descricao = binding.etdDescricao.getText().toString().trim();

            // Valida se os campos obrigatórios estão preenchidos
            if (nomeProduto.isEmpty() || tamanho.isEmpty() || cor.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return; // Impede o prosseguimento se os campos estiverem vazios
            }

            // Valida se a imagem foi capturada
            if (imgProduto == null) {
                Toast.makeText(getContext(), "Por favor, capture uma foto do produto.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Converte o Bitmap da imagem para um array de bytes para salvar no Room
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imgProduto.compress(Bitmap.CompressFormat.PNG, 100, stream); // Comprime como PNG
            byte[] fotoEmBytes = stream.toByteArray();

            // Cria um novo objeto Produto
            Produto novoProduto = new Produto();
            novoProduto.nome = nomeProduto;
            novoProduto.tamanho = tamanho;
            novoProduto.cor = cor;
            novoProduto.descricao = descricao;
            novoProduto.foto = fotoEmBytes;

            // Executa a operação de inserção no banco de dados em uma thread de background
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    produtoDao.inserir(novoProduto);
                    // Retorna para a thread UI para exibir o Toast de sucesso
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Produto cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                            // Opcional: Limpar os campos após o cadastro
                            binding.edtProduto.setText("");
                            binding.edtTamanho.setText("");
                            binding.edtCor.setText("");
                            binding.etdDescricao.setText("");
                            binding.imgViewFoto.setImageResource(R.drawable.outline_add_a_photo_24); // Redefine a imagem padrão
                            imgProduto = null; // Limpa o bitmap da imagem
                        });
                    }
                } catch (Exception e) {
                    // Retorna para a thread UI para exibir o Toast de erro
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Erro ao cadastrar produto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}