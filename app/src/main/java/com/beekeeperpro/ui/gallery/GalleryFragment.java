package com.beekeeperpro.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.databinding.GalleryFragmentBinding;

public class GalleryFragment extends Fragment {

    private GalleryFragmentBinding binding;
    private GalleryViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = GalleryFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerView = binding.gallery;
        GalleryAdapter adapter = new GalleryAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getImages().observe(getViewLifecycleOwner(), adapter::setPictures);
        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        viewModel.update(requireContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}