package com.beekeeperpro.ui.hive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.databinding.FindHiveFragmentBinding;

import java.sql.SQLException;

public class FindHiveFragment extends Fragment {
    FindHiveFragmentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FindHiveFragmentBinding.inflate(inflater, container, false);
        binding.searchBt.setOnClickListener(v -> loadHive(binding.hiveCode.getText().toString()));
        return binding.getRoot();
    }

    private void loadHive(String code){
        try {
            Hive hive = Hive.select(code);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle args = new Bundle();
            args.putParcelable("hive", hive);
            args.putParcelable("apiary", hive.getApiary());
            navController.navigate(R.id.action_nav_find_hive_to_hive, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}