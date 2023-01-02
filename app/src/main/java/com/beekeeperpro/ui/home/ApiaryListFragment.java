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
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.databinding.FragmentApiaryListBinding;
import com.beekeeperpro.ui.menu.EditMenuProvider;

public class ApiaryListFragment extends Fragment{

    private ApiaryListViewModel viewModel;
    private EditMenuProvider editMenu;

    public ApiaryListFragment(){}
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ApiaryListViewModel.class);

        FragmentApiaryListBinding binding = FragmentApiaryListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ApiaryListAdapter adapter = new ApiaryListAdapter();
        adapter.getClickedId().observe(getViewLifecycleOwner(), this::onClick);
        adapter.getOnDeleteItem().observe(getViewLifecycleOwner(), id -> viewModel.delete(id));
        binding.ApiaryList.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), adapter::setApiaries);
        editMenu = getEditMenu(adapter);
        return root;
    }

    @NonNull
    private EditMenuProvider getEditMenu(ApiaryListAdapter adapter) {
        return new EditMenuProvider() {
            @Override
            protected void onEditButton() {
                adapter.setEditMode(true);
            }

            @Override
            protected void onFinishButton() {
                adapter.setEditMode(false);
            }
        };
    }

    private void onClick(Apiary apiary) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        Bundle args = new Bundle();
        args.putInt("id", apiary.getId());
        navController.navigate(R.id.action_nav_home_to_apiaryFragment, args);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.fab).setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_nav_home_to_addApiaryFragment));
        viewModel.update();
        requireActivity().addMenuProvider(editMenu);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.fab).setOnClickListener(null);
        requireActivity().removeMenuProvider(editMenu);
    }
}