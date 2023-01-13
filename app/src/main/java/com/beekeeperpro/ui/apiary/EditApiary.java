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
import android.widget.ImageView;
import android.widget.TextView;
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
import com.beekeeperpro.databinding.ApiaryEditFragmentBinding;
import com.beekeeperpro.ui.menu.SaveMenuProvider;
import com.beekeeperpro.utils.Location;
import com.beekeeperpro.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditApiary extends Fragment implements View.OnClickListener {
    private SaveMenuProvider saveMenu;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean savePushed = false;
    private Uri photoFile;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private EditApiaryViewModel viewModel;
    private ApiaryEditFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ApiaryEditFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EditApiaryViewModel.class);
        viewModel.initWith(getArguments().getParcelable("apiary"));
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
        view.findViewById(R.id.locationBt).setOnClickListener(this);
        saveMenu = new SaveMenuProvider() {
            @Override
            protected void onSaveButton() {
                saveToViewModel();
                savePushed = viewModel.save();
            }
        };
        view.findViewById(R.id.image).setOnClickListener(v -> {
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
                            ((ImageView)view.findViewById(R.id.image)).setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return view;
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
        ((TextView)binding.getRoot().findViewById(R.id.apiaryName)).setText(apiary.getName());
        ((TextView)binding.getRoot().findViewById(R.id.apiaryLocation)).setText(apiary.getLocation());
        ((TextView)binding.getRoot().findViewById(R.id.apiaryLat)).setText(apiary.getCoordinate().getLatitude() + "");
        ((TextView)binding.getRoot().findViewById(R.id.apiaryLong)).setText(apiary.getCoordinate().getLongitude() + "");
        binding.getRoot().findViewById(R.id.contentTable).post(() -> {
            if(apiary.getPicture() != null) {
                ((ImageView)binding.getRoot().findViewById(R.id.image)).setImageBitmap(apiary.getPicture());
            }
        });
    }

    private void saveToViewModel() {
        Apiary apiary = viewModel.getData().getValue();
        apiary.setName(((TextView)binding.getRoot().findViewById(R.id.apiaryName)).getText().toString());
        apiary.setLocation(((TextView)binding.getRoot().findViewById(R.id.apiaryLocation)).getText().toString());
        if(photoFile != null) {
            try {
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(photoFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                apiary.setPicture(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            apiary.setCoordinate(Double.parseDouble(((TextView)binding.getRoot().findViewById(R.id.apiaryLat)).getText().toString()),
                    Double.parseDouble(((TextView)binding.getRoot().findViewById(R.id.apiaryLong)).getText().toString()));
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