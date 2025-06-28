package com.example.elasclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elasclub.data.Produto;

import java.util.List;

public class AdapterListView extends ArrayAdapter<Produto> {

    private Context mContext;
    private List<Produto> produtoList;

    public AdapterListView(@NonNull Context context, @NonNull List<Produto> produtoList) {
        super(context, 0, produtoList);
        this.mContext = context;
        this.produtoList = produtoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        }

        Produto currentProduto = produtoList.get(position);

        ImageView imgViewItemFoto = listItem.findViewById(R.id.imageView);
        TextView txtItemNomeProduto = listItem.findViewById(R.id.txtNomeProduto);
        TextView txtItemDescricao = listItem.findViewById(R.id.txtDescricao);
        TextView txtItemTamanhoCor = listItem.findViewById(R.id.txtTamanhoCor);

        // Define o nome do produto
        txtItemNomeProduto.setText(currentProduto.nome);

        // Define a descrição
        txtItemDescricao.setText(currentProduto.descricao);

        // Define tamanho e cor juntos
        String tamanhoCor = "Tamanho: " + currentProduto.tamanho + ", Cor: " + currentProduto.cor;
        txtItemTamanhoCor.setText(tamanhoCor);

        // Define a foto do produto
        if (currentProduto.foto != null && currentProduto.foto.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(currentProduto.foto, 0, currentProduto.foto.length);
            imgViewItemFoto.setImageBitmap(bitmap);
        } else {
            // Se não houver foto, define uma imagem placeholder
            imgViewItemFoto.setImageResource(R.drawable.outline_add_a_photo_24);
        }

        return listItem;
    }

    // Método para atualizar os dados do adapter
    public void setProdutos(List<Produto> produtos) {
        produtoList.clear();
        produtoList.addAll(produtos);
        notifyDataSetChanged(); // Notifica o ListView que os dados mudaram
    }
}
