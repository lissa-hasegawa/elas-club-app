package com.example.elasclub.ui.actions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elasclub.R;
import com.example.elasclub.databinding.FragmentCadastroProdutoBinding;

public class CadastroProdutoFragment extends Fragment {

    private FragmentCadastroProdutoBinding binding;

    public CadastroProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCadastroProdutoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}