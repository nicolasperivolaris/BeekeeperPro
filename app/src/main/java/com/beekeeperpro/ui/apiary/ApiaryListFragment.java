package com.beekeeperpro.ui.apiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.databinding.ApiaryListFragmentBinding;
import com.beekeeperpro.ui.menu.EditMenuProvider;

public class ApiaryListFragment extends Fragment {

    private ApiaryListViewModel viewModel;
    private EditMenuProvider editMenu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ApiaryListViewModel.class);
        ApiaryListFragmentBinding binding = ApiaryListFragmentBinding.inflate(inflater, container, false);
        ApiaryListAdapter adapter = new ApiaryListAdapter();
        adapter.getClickedId().observe(getViewLifecycleOwner(), this::onClick);
        adapter.getOnDeleteItem().observe(getViewLifecycleOwner(), apiary -> viewModel.delete(apiary));
        adapter.getOnEditItem().observe(getViewLifecycleOwner(), this::onEdit);
        binding.apiaryList.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), adapter::setApiaries);
        viewModel.getErrors().observe(getViewLifecycleOwner(), (e) -> Toast.makeText(requireContext(), e.getError().toString(), Toast.LENGTH_LONG).show());
        editMenu = getEditMenu(adapter);
        return binding.getRoot();
    }

    private void onEdit(Apiary apiary) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        Bundle args = new Bundle();
        args.putParcelable("apiary", apiary);
        navController.navigate(R.id.action_nav_apiary_list_to_edit_apiary, args);
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
        args.putParcelable("apiary", apiary);
        navController.navigate(R.id.action_nav_apiary_list_to_hive_list_fragment, args);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.fab).setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_nav_apiary_list_to_addApiaryFragment));
        viewModel.select();
        requireActivity().addMenuProvider(editMenu);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.fab).setOnClickListener(null);
        requireActivity().removeMenuProvider(editMenu);
    }
}