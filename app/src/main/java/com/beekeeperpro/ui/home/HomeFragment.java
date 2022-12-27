package com.beekeeperpro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.beekeeperpro.MainActivity;
import com.beekeeperpro.data.ApiaryRepository;
import com.beekeeperpro.databinding.FragmentHomeBinding;
import com.google.android.gms.common.api.Api;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ApiariesAdapter adapter = new ApiariesAdapter();
        binding.ApiaryList.setAdapter(adapter);
        homeViewModel.getApiaries().observe(getViewLifecycleOwner(), adapter::setApiaries);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel.update();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}