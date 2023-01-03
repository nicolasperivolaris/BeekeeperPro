package com.beekeeperpro.ui.hive;

import android.app.DatePickerDialog;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.menu.SaveMenuProvider;
import com.beekeeperpro.utils.BPSeekbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddHiveFragment extends Fragment {

    private SaveMenuProvider saveMenu;
    private AddHiveViewModel viewModel;
    private boolean saving = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        saveMenu = new SaveMenuProvider() {
            @Override
            protected void onSaveButton() {
                saveToViewModel();
                if(viewModel.save()){
                    saving = true;
                }
            }
        };

        return inflater.inflate(R.layout.add_hive_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AddHiveViewModel.class);
        viewModel.setApiary(getArguments().getParcelable("apiary"));

        view.findViewById(R.id.hiveHivingDate).setOnClickListener(view1 -> initDatePicker((EditText) view1));
        view.findViewById(R.id.hiveAcquisitionDate).setOnClickListener(view1 -> initDatePicker((EditText) view1));

        BPSeekbar strengthBar = view.findViewById(R.id.hiveSeekbar);
        strengthBar.setValues(getResources().getStringArray(R.array.strength_bar_values));
        strengthBar.setMin(0);
        strengthBar.setMax(getResources().getStringArray(R.array.strength_bar_values).length-1);

        viewModel.getData().observe(getViewLifecycleOwner(), hive -> {
            if(saving) {
                NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                nav.popBackStack();
                Toast.makeText(getContext(), "Saved !", Toast.LENGTH_LONG).show();
                saving = false;
            }
        });
        viewModel.getErrors().observe(getViewLifecycleOwner(), error -> Toast.makeText(getContext(), "Internal error", Toast.LENGTH_LONG).show());
        viewModel.getValidationError().observe(getViewLifecycleOwner(), error -> Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show());

    }

    private void initDatePicker(EditText field){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crée un date picker dans le format de date local
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (datePicker, year1, month1, day1) -> {
                    // Mise à jour du champ de date avec la date sélectionnée
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, year1);
                    calendar1.set(Calendar.MONTH, month1);
                    calendar1.set(Calendar.DAY_OF_MONTH, day1);
                    Date date = calendar1.getTime();
                    field.setText(dateFormat.format(date));
                },
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromViewModel();
        requireActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        requireActivity().addMenuProvider(saveMenu);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveToViewModel();
        requireActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        requireActivity().removeMenuProvider(saveMenu);
    }

    private void loadFromViewModel() {
        Hive hive = viewModel.getData().getValue();
        if(hive == null) return;
        ((TextView)requireView().findViewById(R.id.hiveName)).setText(hive.getName());
        ((TextView)requireView().findViewById(R.id.hiveCode)).setText(hive.getCode());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        if(hive.getHivingDate() != null)((TextView)requireView().findViewById(R.id.hiveHivingDate)).setText(dateFormat.format(hive.getHivingDate()));
        if(hive.getAcquisitionDate() != null)((TextView)requireView().findViewById(R.id.hiveAcquisitionDate)).setText(dateFormat.format(hive.getAcquisitionDate()));
        ((BPSeekbar)requireView().findViewById(R.id.hiveSeekbar)).setProgress(hive.getStrength());
    }

    private void saveToViewModel() {
        Hive hive = new Hive();
        hive.setName(((TextView)requireView().findViewById(R.id.hiveName)).getText().toString());
        hive.setCode(((TextView)requireView().findViewById(R.id.hiveCode)).getText().toString());
        hive.setStrength(((BPSeekbar)requireView().findViewById(R.id.hiveSeekbar)).getProgress());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        try {
            hive.setHivingDate(dateFormat.parse(((TextView)requireView().findViewById(R.id.hiveHivingDate)).getText().toString()));
        } catch (ParseException e) {
            hive.setHivingDate(null);
        }
        try {
            hive.setAcquisitionDate(dateFormat.parse(((TextView)requireView().findViewById(R.id.hiveAcquisitionDate)).getText().toString()));
        } catch (ParseException e) {
            hive.setAcquisitionDate(null);
        }
        viewModel.getData().setValue(hive);
    }
}