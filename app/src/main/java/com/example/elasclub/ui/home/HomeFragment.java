package com.example.elasclub.ui.home;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elasclub.R;
import com.example.elasclub.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private GoogleMap googleMap;
    private Geocoder geocoder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa o Geocoder
        geocoder = new Geocoder(getContext(), new Locale("pt", "BR")); // Definir localidade para resultados em português, se possível

        // Configura o SupportMapFragment
        // Note: o ID 'map' no XML deve corresponder ao ID do fragmento do mapa.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Inicia o carregamento do mapa e chama onMapReady quando pronto
        }

        binding.btnCadastro.setOnClickListener(v ->
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_navigation_home_to_navigation_cadastro));

        binding.btnLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_navigation_home_to_navigation_login));

        binding.btnEndereco.setOnClickListener(v -> buscarLocPeloEndereco());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        LatLng saoPaulo = new LatLng(-23.55052, -46.633309); // Coordenadas de São Paulo
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 10)); // Zoom de 10
    }

    private void buscarLocPeloEndereco() {
        String enderecoStr = binding.edtTitulo.getText().toString().trim();

        if (enderecoStr.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, digite um endereço.", Toast.LENGTH_SHORT).show();
            return;
        }


        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(enderecoStr, 1); // Busca 1 resultado

                if (addresses != null && !addresses.isEmpty()) {
                    Address returnedAddress = addresses.get(0);
                    LatLng latLng = new LatLng(returnedAddress.getLatitude(), returnedAddress.getLongitude());

                    // Atualiza a UI na thread principal
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (googleMap != null) {
                                googleMap.clear(); // Limpa marcadores anteriores
                                googleMap.addMarker(new MarkerOptions().position(latLng).title(enderecoStr));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom de 15
                                Toast.makeText(getContext(), "Endereço encontrado!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // Nenhum endereço encontrado
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Endereço não encontrado. Tente novamente.", Toast.LENGTH_LONG).show();
                        });
                    }
                }
            } catch (IOException e) {
                // Erro de rede ou serviço de geocoding indisponível
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Erro ao buscar endereço: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // Endereço inválido
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Endereço inválido.", Toast.LENGTH_LONG).show();
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