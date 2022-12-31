package com.beekeeperpro.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.MenuProviderFragment;
import com.beekeeperpro.utils.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class AddApiaryFragment extends MenuProviderFragment implements View.OnClickListener {

    private AddApiaryViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;

    public AddApiaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddApiaryViewModel.class);
        viewModel.getValidationError().observe(getViewLifecycleOwner(), s -> Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show());
        viewModel.getErrors().observe(getViewLifecycleOwner(), s -> Toast.makeText(getContext(), s.getError().toString(), Toast.LENGTH_LONG).show());
        View view = inflater.inflate(R.layout.fragment_add_apiary, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        view.findViewById(R.id.locationBt).setOnClickListener(this);
        return view;
    }

    private void addSaveButton() {
        MenuItem saveBt = ((Toolbar) requireActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.save_button);
        saveBt.setVisible(true);
        saveBt.setOnMenuItemClickListener(item -> {
            saveToViewModel();
            if(viewModel.save()){
                NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                Bundle args = new Bundle();
                args.putParcelable("apiary", viewModel.getData().getValue());
                nav.navigate(R.id.action_addApiaryFragment_to_nav_home, args);
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromViewModel();
        requireActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }

    private void loadFromViewModel() {
        Apiary apiary = viewModel.getData().getValue();
        if(apiary == null) return;
        ((TextView)requireView().findViewById(R.id.apiaryName)).setText(apiary.getName());
        ((TextView)requireView().findViewById(R.id.apiaryLocation)).setText(apiary.getLocation());
        ((TextView)requireView().findViewById(R.id.apiaryLat)).setText(apiary.getCoordinate().getLatitude()+"");
        ((TextView)requireView().findViewById(R.id.apiaryLong)).setText(apiary.getCoordinate().getLongitude()+"");
    }

    private void saveToViewModel() {
        Apiary apiary = new Apiary();
        apiary.setName(((TextView)requireView().findViewById(R.id.apiaryName)).getText().toString());
        apiary.setLocation(((TextView)requireView().findViewById(R.id.apiaryLocation)).getText().toString());
        apiary.setCoordinate(Double.parseDouble(((TextView)requireView().findViewById(R.id.apiaryLat)).getText().toString()),
                Double.parseDouble(((TextView)requireView().findViewById(R.id.apiaryLat)).getText().toString()));
        viewModel.getData().setValue(apiary);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveToViewModel();
        requireActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        requireActivity().invalidateOptionsMenu();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

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

    @Override
    protected void onSaveButton(){
        saveToViewModel();
        if(viewModel.save()){
            NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle args = new Bundle();
            args.putParcelable("apiary", viewModel.getData().getValue());
            nav.navigate(R.id.action_addApiaryFragment_to_nav_home, args);
        }
    }
}