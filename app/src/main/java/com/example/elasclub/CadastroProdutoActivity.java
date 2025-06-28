package com.example.elasclub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import com.example.elasclub.data.AppDatabase;
import com.example.elasclub.data.Produto;
import com.example.elasclub.data.ProdutoDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Executors;

public class CadastroProdutoActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE=1;
    ActivityResultLauncher<Uri> takePictureLauncher;
    Uri imageUri;
    private EditText edtProduto, edtTamanho, edtCor, edtDescricao;
    private Button btnNovoProduto, btnVoltarProduto;
    private ImageView imgFoto;
    private Bitmap fotoBitmap;
    private ProdutoDao produtoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        edtProduto = findViewById(R.id.edtProduto);
        edtTamanho = findViewById(R.id.edtTamanho);
        edtCor = findViewById(R.id.edtCor);
        edtDescricao = findViewById(R.id.edtDescricao);
        imgFoto = findViewById(R.id.imgViewFoto);
        btnNovoProduto = findViewById(R.id.btnNovoProduto);
        btnVoltarProduto = findViewById(R.id.btnVoltarProduto);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        produtoDao = db.produtoDao();

        imageUri = createUri();
        registerPictureLauncher();

        btnNovoProduto.setOnClickListener(v -> cadastrarProduto());
        btnVoltarProduto.setOnClickListener(v -> finish());
    }

    private void cadastrarProduto() {
        String produto = edtProduto.getText().toString();
        String tamanho = edtTamanho.getText().toString();
        String cor = edtCor.getText().toString();
        String descricao = edtDescricao.getText().toString();

        if (produto.isEmpty() || tamanho.isEmpty() || cor.isEmpty() || descricao.isEmpty() || fotoBitmap == null) {
            Toast.makeText(this, "Preencha todos os campos e tire uma foto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Converte a imagem para byte[]
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        fotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] fotoBytes = stream.toByteArray();

        Produto produto1 = new Produto();
        produto1.nome = produto;
        produto1.tamanho = tamanho;
        produto1.cor = cor;
        produto1.descricao = descricao;
        produto1.foto = fotoBytes;

        // Executa a operação de inserção em uma thread de background
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                produtoDao.inserir(produto1);
                runOnUiThread(() -> { // Volta para a thread UI para o Toast e limpar campos
                    Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    limparCampos();
                    finish(); // Fecha a Activity após o cadastro
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro ao cadastrar produto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
                e.printStackTrace();
            }
        });
    }

    private void limparCampos() {
        edtProduto.setText("");
        edtTamanho.setText("");
        edtCor.setText("");
        edtDescricao.setText("");
        imgFoto.setImageResource(R.drawable.outline_add_a_photo_24); // imagem default
        fotoBitmap = null;
    }

    public void capturarImagem(View view) {
        checkCameraPermissionAndOpenCamera();
    }
    private Uri createUri(){
        File imageFile=new File(getApplicationContext().getFilesDir(),"camera_photo.jpg");
        return FileProvider.getUriForFile(getApplicationContext(),
                "com.example.elasclub.fileprovider", imageFile );
    }

    private void registerPictureLauncher() {
        takePictureLauncher=registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean o) {
                        try{
                            if(o){
                                imgFoto.setImageURI(null);
                                imgFoto.setImageURI(imageUri);

                                fotoBitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(),
                                        imageUri);
                            }
                        }catch (Exception exception){
                            exception.getStackTrace();
                        }
                    }
                }
        );
    }

    private void checkCameraPermissionAndOpenCamera() {
        if(ActivityCompat.checkSelfPermission(CadastroProdutoActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CadastroProdutoActivity.this,
                    new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
        }else{
            Toast.makeText(this,"Check Permission Granted", Toast.LENGTH_SHORT).show();
            takePictureLauncher.launch(imageUri);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Request Permission Granted",Toast.LENGTH_SHORT).show();
                takePictureLauncher.launch(imageUri);
            }else{
                Toast.makeText(this,"Request Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}