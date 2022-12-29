package com.beekeeperpro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        com.beekeeperpro.databinding.FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ApiaryAdapter adapter = new ApiaryAdapter();
        adapter.getClickedId().observe(getViewLifecycleOwner(), this::onClick);
        binding.ApiaryList.setAdapter(adapter);
        homeViewModel.getData().observe(getViewLifecycleOwner(), adapter::setApiaries);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel.update();
    }

    private void onClick(int id) {
        //int position = adapter.getAdapterPosition();
        //int id = apiaries.get(position).getId();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        Bundle args = new Bundle();
        args.putInt("id", id);
        navController.navigate(R.id.action_nav_home_to_apiaryFragment, args);
    }
}