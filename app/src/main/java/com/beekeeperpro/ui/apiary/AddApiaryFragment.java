package com.beekeeperpro.ui.apiary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.menu.SaveMenuProvider;
import com.beekeeperpro.utils.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class AddApiaryFragment extends Fragment implements View.OnClickListener {

    private SaveMenuProvider saveMenu;
    private AddApiaryViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean savePushed = false;

    public AddApiaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apiary_add_fragment, container, false);

        super.onViewCreated(view, savedInstanceState);
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
        view.findViewById(R.id.locationBt).setOnClickListener(this);
        saveMenu = new SaveMenuProvider() {
            @Override
            protected void onSaveButton() {
                saveToViewModel();
                savePushed = viewModel.save();
            }
        };

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        requireActivity().addMenuProvider(saveMenu);
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
        ((TextView) requireView().findViewById(R.id.apiaryName)).setText(apiary.getName());
        ((TextView) requireView().findViewById(R.id.apiaryLocation)).setText(apiary.getLocation());
        ((TextView) requireView().findViewById(R.id.apiaryLat)).setText(apiary.getCoordinate().getLatitude() + "");
        ((TextView) requireView().findViewById(R.id.apiaryLong)).setText(apiary.getCoordinate().getLongitude() + "");
    }

    private void saveToViewModel() {
        Apiary apiary = viewModel.getData().getValue();
        apiary.setName(((TextView) requireView().findViewById(R.id.apiaryName)).getText().toString());
        apiary.setLocation(((TextView) requireView().findViewById(R.id.apiaryLocation)).getText().toString());
        try {
            apiary.setCoordinate(Double.parseDouble(((TextView) requireView().findViewById(R.id.apiaryLat)).getText().toString()),
                    Double.parseDouble(((TextView) requireView().findViewById(R.id.apiaryLat)).getText().toString()));
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