package com.beekeeperpro.ui.apiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.beekeeperpro.databinding.FragmentApiaryBinding;

public class ApiaryFragment extends Fragment {

    private ApiaryViewModel viewModel;

    public ApiaryFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ApiaryViewModel.class);
        viewModel.setApiaryId(getArguments().getInt("id"));
        FragmentApiaryBinding binding = FragmentApiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        HiveAdapter adapter = new HiveAdapter();
        adapter.getClickedId().observe(getViewLifecycleOwner(), integer -> {});
        binding.hiveList.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), adapter::setHiveList);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.update();
    }
}