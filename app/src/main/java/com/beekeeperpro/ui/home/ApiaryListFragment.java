package com.beekeeperpro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.databinding.FragmentApiaryListBinding;

public class ApiaryListFragment extends Fragment{

    private ApiaryListViewModel apiaryListViewModel;

    public ApiaryListFragment(){}
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        apiaryListViewModel = new ViewModelProvider(this).get(ApiaryListViewModel.class);

        FragmentApiaryListBinding binding = FragmentApiaryListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ApiaryListAdapter adapter = new ApiaryListAdapter();
        adapter.getClickedId().observe(getViewLifecycleOwner(), this::onClick);
        binding.ApiaryList.setAdapter(adapter);
        apiaryListViewModel.getData().observe(getViewLifecycleOwner(), adapter::setApiaries);
        return root;
    }

    private void onClick(int id) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        Bundle args = new Bundle();
        args.putInt("id", id);
        navController.navigate(R.id.action_nav_home_to_apiaryFragment, args);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.fab).setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_nav_home_to_addApiaryFragment);
        });
        apiaryListViewModel.update();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.fab).setOnClickListener(null);
    }
}