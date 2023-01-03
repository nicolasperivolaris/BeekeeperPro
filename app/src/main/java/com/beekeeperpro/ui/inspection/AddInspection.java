package com.beekeeperpro.ui.inspection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.databinding.AddInspectionFragmentBinding;
import com.beekeeperpro.ui.menu.SaveMenuProvider;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class AddInspection extends Fragment {

    private AddInspectionViewModel viewModel;
    private SaveMenuProvider saveMenu;

    public static AddInspection newInstance() {
        return new AddInspection();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        AddInspectionFragmentBinding binding = AddInspectionFragmentBinding.inflate(inflater, container, false);

        saveMenu = new SaveMenuProvider() {
            @Override
            protected void onSaveButton() {
                saveToViewModel();
                viewModel.save();
            }
        };

        return binding.getRoot();
    }

    private void saveToViewModel() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AddInspectionViewModel(getArguments().getParcelable("hive"));
            }
        }).get(AddInspectionViewModel.class);

        ChipGroup chipGroup = requireActivity().findViewById(R.id.attention_points_chipgroup);
        for (String keyword : getResources().getStringArray(R.array.attention_points)) {
            Chip chip = new Chip(getContext());
            chip.setText(keyword);
            chip.setCheckable(true);
            chipGroup.addView(chip);
        }

        Hive hive = getArguments().getParcelable("hive");
        String hiveName = hive.getName().equals("") ? hive.getCode() : hive.getName();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.hive_inspection_title, hiveName, hive.getApiary().getName()));

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveToViewModel();
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


}