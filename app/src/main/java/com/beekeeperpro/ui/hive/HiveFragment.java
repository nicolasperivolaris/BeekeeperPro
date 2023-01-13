package com.beekeeperpro.ui.hive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.databinding.HiveFragmentBinding;
import com.beekeeperpro.ui.inspection.InspectionListAdapter;
import com.beekeeperpro.ui.menu.EditMenuProvider;
import com.beekeeperpro.utils.BPSeekbar;
import com.beekeeperpro.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;

public class HiveFragment extends Fragment {
    private EditMenuProvider editMenu;
    private HiveViewModel viewModel;
    private HiveFragmentBinding binding;
    private boolean deletedPushed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = HiveFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HiveViewModel.class);
        viewModel.initWith(getArguments().getParcelable("hive"));

        InspectionListAdapter adapter = new InspectionListAdapter();
        /*adapter.getOnClickedItem().observe(getViewLifecycleOwner(), hive -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle args = new Bundle();
            args.putParcelable("hive", hive);
            navController.navigate(R.id.action_hive_fragment_to_add_Inspection_fragment, args);
        });*/
        adapter.getOnDeleteItem().observe(getViewLifecycleOwner(), inspection -> {
            deletedPushed = true;
            viewModel.delete(inspection);
        });
        binding.inspectionList.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), hive -> {loadFromViewModel();});
        viewModel.getDone().observe(getViewLifecycleOwner(), saved -> {
            if(deletedPushed){
                viewModel.select();
                deletedPushed = false;
            }
            else loadFromViewModel();
            Toast.makeText(getContext(), "Saved !", Toast.LENGTH_LONG).show();
        });
        viewModel.getErrors().observe(getViewLifecycleOwner(), s -> {
            deletedPushed = false;
            Toast.makeText(getContext(), s.getError().toString(), Toast.LENGTH_LONG).show();
        });

        editMenu = getEditMenu(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BPSeekbar)view.findViewById(R.id.hiveSeekbar)).setValues(getResources().getStringArray(R.array.strength_bar_values));

        binding.hiveHivingDate.setOnClickListener(view1 -> Utils.initDatePicker((EditText) view1));
        binding.hiveAcquisitionDate.setOnClickListener(view1 -> Utils.initDatePicker((EditText) view1));
    }

    @NonNull
    private EditMenuProvider getEditMenu(InspectionListAdapter adapter) {
        return new EditMenuProvider() {
            @Override
            protected void onEditButton() {
                adapter.setEditMode(true);
                setFieldEditable(true);
            }

            @Override
            protected void onFinishButton() {
                adapter.setEditMode(false);
                setFieldEditable(false);
                saveToViewModel();
                viewModel.save();
            }
        };
    }

    private void setFieldEditable(boolean edit){
        binding.hiveName.setEnabled(edit);
        binding.hiveCode.setEnabled(edit);
        binding.hiveSeekbar.setEnabled(edit);
        binding.hiveHivingDate.setEnabled(edit);
        binding.hiveAcquisitionDate.setEnabled(edit);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.fab).setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putParcelable("hive", viewModel.getData().getValue());
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_hive_fragment_to_add_Inspection_fragment, arg);
        });

        loadFromViewModel();
        setFieldEditable(false);
        requireActivity().addMenuProvider(editMenu);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().removeMenuProvider(editMenu);
        saveToViewModel();
    }


    private void loadFromViewModel() {
        Hive hive = viewModel.getData().getValue();
        if (hive == null) return;
        ((TextView) requireView().findViewById(R.id.hiveName)).setText(hive.getName());
        ((TextView) requireView().findViewById(R.id.hiveCode)).setText(hive.getCode());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        if (hive.getHivingDate() != null)
            ((TextView) requireView().findViewById(R.id.hiveHivingDate)).setText(dateFormat.format(hive.getHivingDate()));
        if (hive.getAcquisitionDate() != null)
            ((TextView) requireView().findViewById(R.id.hiveAcquisitionDate)).setText(dateFormat.format(hive.getAcquisitionDate()));
        ((BPSeekbar) requireView().findViewById(R.id.hiveSeekbar)).setProgress(hive.getStrength());
        ((InspectionListAdapter)binding.inspectionList.getAdapter()).setInspectionList((viewModel.getData().getValue()).getInspectionList());
    }

    private void saveToViewModel() {
        Hive hive = viewModel.getData().getValue();
        hive.setName(((TextView) requireView().findViewById(R.id.hiveName)).getText().toString());
        hive.setCode(((TextView) requireView().findViewById(R.id.hiveCode)).getText().toString());
        hive.setStrength(((BPSeekbar) requireView().findViewById(R.id.hiveSeekbar)).getProgress());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        try {
            hive.setHivingDate(dateFormat.parse(((TextView) requireView().findViewById(R.id.hiveHivingDate)).getText().toString()));
        } catch (ParseException e) {
            hive.setHivingDate(null);
        }
        try {
            hive.setAcquisitionDate(dateFormat.parse(((TextView) requireView().findViewById(R.id.hiveAcquisitionDate)).getText().toString()));
        } catch (ParseException e) {
            hive.setAcquisitionDate(null);
        }
    }
}