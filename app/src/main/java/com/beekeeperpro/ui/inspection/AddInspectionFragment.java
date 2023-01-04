package com.beekeeperpro.ui.inspection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.Inspection;
import com.beekeeperpro.databinding.InspectionAddFragmentBinding;
import com.beekeeperpro.ui.menu.SaveMenuProvider;
import com.beekeeperpro.utils.Utils;
import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

public class AddInspectionFragment extends Fragment {
    private boolean savePushed = false;
    private AddInspectionViewModel viewModel;
    private SaveMenuProvider saveMenu;
    private InspectionAddFragmentBinding binding;

    public static AddInspectionFragment newInstance() {
        return new AddInspectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = InspectionAddFragmentBinding.inflate(inflater, container, false);
        saveMenu = new SaveMenuProvider() {
            @Override
            protected void onSaveButton() {
                saveToViewModel();
                savePushed = viewModel.save();
            }
        };

        return binding.getRoot();
    }

    private void saveToViewModel() {
        Inspection inspection = viewModel.getData().getValue();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        try {
            inspection.setInspectionDate(dateFormat.parse(binding.inspectionDate.getText().toString()));
        } catch (ParseException e) {
            inspection.setInspectionDate(null);
        }
        inspection.setTemper(binding.temperSpinner.getSelectedItem().toString());
        inspection.setHiveCondition(binding.hiveConditionSpinner.getSelectedItem().toString());
        inspection.setHiveConditionRemarks(binding.hiveConditionRemarks.getText().toString());
        inspection.setQueenCondition(binding.queenConditionSpinner.getSelectedItem().toString());
        inspection.setQueenConditionRemarks(binding.queenConditionRemarks.getText().toString());
        inspection.setPhytosanitaryUsed(binding.phytosanitaryUsedSpinner.getSelectedItem().toString());
        inspection.setPhytosanitaryRemarks(binding.phytosanitayUsedRemarks.getText().toString());
        HashSet<String> attPoints = new HashSet<>();
        for (int c : binding.attentionPointsChipgroup.getCheckedChipIds()) {
            attPoints.add(((Chip) binding.getRoot().findViewById(c)).getText().toString());
        }
        inspection.setAttentionPoints(attPoints);
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
        viewModel.getValidationError().observe(getViewLifecycleOwner(), s -> Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show());
        viewModel.getData().observe(getViewLifecycleOwner(), hive -> {
            if (!savePushed) return;
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).popBackStack();
            Toast.makeText(getContext(), "Saved !", Toast.LENGTH_LONG).show();
        });
        viewModel.getErrors().observe(getViewLifecycleOwner(), error -> Toast.makeText(getContext(), "Internal error", Toast.LENGTH_LONG).show());
        binding.inspectionDate.setOnClickListener(view1 -> Utils.initDatePicker((EditText) view1));

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Hive hive = getArguments().getParcelable("hive");
        String hiveName = hive.getName().equals("") ? hive.getCode() : hive.getName();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.hive_inspection_title, hiveName, hive.getApiary().getName()));

        Inspection inspection = viewModel.getData().getValue();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        binding.inspectionDate.setText(dateFormat.format(inspection.getInspectionDate()));
        binding.temperSpinner.setSelection(Utils.convertStringToArrayPosition(inspection.getTemper(), R.array.temper_values));
        binding.hiveConditionSpinner.setSelection(Utils.convertStringToArrayPosition(inspection.getHiveCondition(), R.array.hive_condition_values));
        binding.hiveConditionRemarks.setText(inspection.getHiveConditionRemarks());
        binding.queenConditionSpinner.setSelection(Utils.convertStringToArrayPosition(inspection.getQueenCondition(), R.array.queen_condition_values));
        binding.queenConditionRemarks.setText(inspection.getQueenConditionRemarks());
        binding.phytosanitaryUsedSpinner.setSelection(Utils.convertStringToArrayPosition(inspection.getPhytosanitaryUsed(), R.array.phytosanitary_values));
        binding.phytosanitayUsedRemarks.setText(inspection.getPhytosanitaryRemarks());
        Set<String> attPoints = inspection.getAttentionPoints();
        for (String keyword : getResources().getStringArray(R.array.attention_points)) {
            Chip chip = new Chip(getContext());
            chip.setText(keyword);
            chip.setCheckable(true);
            if (attPoints.contains(keyword))
                chip.setChecked(true);
            binding.attentionPointsChipgroup.addView(chip);
        }
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