package com.beekeeperpro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.beekeeperpro.R;

public class ApiaryFragment extends Fragment {

    private ApiaryViewModel ViewModel;
    public static ApiaryFragment newInstance(int apiaryId) {
        return new ApiaryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apiary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModel = new ViewModelProvider(this).get(ApiaryViewModel.class);
        ViewModel.setApiaryId(getArguments().getInt("id"));
    }
}