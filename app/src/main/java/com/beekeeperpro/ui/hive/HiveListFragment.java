package com.beekeeperpro.ui.hive;

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
import com.beekeeperpro.databinding.HiveListFragmentBinding;
import com.beekeeperpro.ui.menu.EditMenuProvider;

public class HiveListFragment extends Fragment {

    EditMenuProvider editMenu;
    private HiveListViewModel viewModel;

    public HiveListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        HiveListFragmentBinding binding = HiveListFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HiveListViewModel.class);
        viewModel.setApiary(getArguments().getParcelable("apiary"));

        HiveListAdapter adapter = new HiveListAdapter();
        adapter.getOnClickedItem().observe(getViewLifecycleOwner(), hive -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle args = new Bundle();
            args.putParcelable("hive", hive);
            navController.navigate(R.id.action_hives_list_fragment_to_inspectionListFragment, args);
        });
        adapter.getOnDeleteItem().observe(getViewLifecycleOwner(), hive -> viewModel.delete(hive));
        adapter.getOnAddInspectionItem().observe(getViewLifecycleOwner(), hive -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle args = new Bundle();
            args.putParcelable("hive", hive);
            navController.navigate(R.id.action_hives_list_fragment_to_addInspection, args);
        });
        binding.hiveList.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), adapter::setHiveList);

        editMenu = getEditMenu(adapter);
        return binding.getRoot();
    }

    @NonNull
    private EditMenuProvider getEditMenu(HiveListAdapter adapter) {
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

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.fab).setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putParcelable("apiary", viewModel.getApiary());
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_hives_list_to_add_hive_fragment, arg);
        });
        viewModel.update();
        requireActivity().addMenuProvider(editMenu);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().removeMenuProvider(editMenu);
    }
}