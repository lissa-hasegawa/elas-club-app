package com.example.elasclub;

import static com.example.elasclub.R.*;

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
import com.example.elasclub.data.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CadastroActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE=1;
    ActivityResultLauncher<Uri> takePictureLauncher;
    Uri imageUri;
    private final String AES_KEY = "1234567890123456"; // AES de 16 bytes
    private EditText edtNome, edtEmail, edtSenha;
    private Button btnCadastrar, btnVoltar;
    private ImageView imgFoto;
    private Bitmap fotoBitmap;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        imgFoto = findViewById(R.id.imgFoto);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnVoltar = findViewById(id.btnVoltar);

        // Inicializa o banco de dados Room
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "meu_banco.db")
                .allowMainThreadQueries() // Apenas para testes; evite em produção
                .build();
        imageUri=createUri();
        registerPictureLauncher();

        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void cadastrarUsuario() {
        String nome = edtNome.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || fotoBitmap == null) {
            Toast.makeText(this, "Preencha todos os campos e tire uma foto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Converte a imagem para byte[]
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        fotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] fotoBytes = stream.toByteArray();

        Usuario usuario = new Usuario();
        usuario.nome = nome;
        usuario.email = email;
        usuario.senha = senha;
        usuario.foto = fotoBytes;

        db.usuarioDao().inserir(usuario);

        Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
        limparCampos();
        finish();
    }

    private void limparCampos() {
        edtNome.setText("");
        edtEmail.setText("");
        edtSenha.setText("");
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
        if(ActivityCompat.checkSelfPermission(CadastroActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CadastroActivity.this,
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