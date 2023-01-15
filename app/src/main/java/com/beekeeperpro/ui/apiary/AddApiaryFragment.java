package com.beekeeperpro.ui.apiary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.databinding.ApiaryAddFragmentBinding;
import com.beekeeperpro.ui.menu.SaveMenuProvider;
import com.beekeeperpro.utils.Location;
import com.beekeeperpro.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddApiaryFragment extends Fragment implements View.OnClickListener {
    private SaveMenuProvider saveMenu;
    private AddApiaryViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean savePushed = false;
    private Uri photoFile;
    private ApiaryAddFragmentBinding binding;
    private ActivityResultLauncher<Uri> cameraLauncher;

    public AddApiaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ApiaryAddFragmentBinding.inflate(inflater, container, false);
        super.onViewCreated(binding.getRoot(), savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AddApiaryViewModel.class);
        viewModel.getValidationError().observe(getViewLifecycleOwner(), s -> Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show());
        viewModel.getErrors().observe(getViewLifecycleOwner(), s -> {
            savePushed = false;
            Toast.makeText(getContext(), s.getError().toString(), Toast.LENGTH_LONG).show();
        });
        viewModel.getDone().observe(getViewLifecycleOwner(), apiary -> {
            if (!savePushed) return;
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).popBackStack();
            Toast.makeText(getContext(), "Saved !", Toast.LENGTH_LONG).show();
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        binding.locationBt.setOnClickListener(this);
        saveMenu = new SaveMenuProvider() {
            @Override
            protected void onSaveButton() {
                saveToViewModel();
                savePushed = viewModel.save();
            }
        };
        binding.image.setOnClickListener(v -> {
            try {
                photoFile = Utils.getEmptyPictureFile(requireContext());
                cameraLauncher.launch(photoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        try {
                            InputStream inputStream = requireActivity().getContentResolver().openInputStream(photoFile);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.image.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        requireActivity().addMenuProvider(saveMenu);
        loadFromViewModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        requireActivity().removeMenuProvider(saveMenu);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        loadFromViewModel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveToViewModel();
    }

    private void loadFromViewModel() {
        Apiary apiary = viewModel.getData().getValue();
        if (apiary == null) return;
        binding.apiaryName.setText(apiary.getName());
        binding.apiaryLocation.setText(apiary.getLocation());
        binding.apiaryLat.setText(apiary.getCoordinate().getLatitude() + "");
        binding.apiaryLong.setText(apiary.getCoordinate().getLongitude() + "");
        binding.contentTable.post(() -> {
            if (apiary.getPicture() != null) {
                binding.image.setImageBitmap(apiary.getPicture());
            }
        });
    }

    private void saveToViewModel() {
        Apiary apiary = viewModel.getData().getValue();
        apiary.setName(binding.apiaryName.getText().toString());
        apiary.setLocation(binding.apiaryLocation.getText().toString());
        if (photoFile != null) {
            try {
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(photoFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                apiary.setPicture(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            apiary.setCoordinate(Double.parseDouble(binding.apiaryLat.getText().toString()),
                    Double.parseDouble(binding.apiaryLong.getText().toString()));
        } catch (NumberFormatException e) {
            System.err.println(e);
            Toast.makeText(getContext(), "Bad coordinate format", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        saveToViewModel();
                        viewModel.getData().getValue().setCoordinate(new Location(location));
                        loadFromViewModel();
                    }
                });
    }
}