package com.beekeeperpro.ui.inspection;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beekeeperpro.R;
import com.beekeeperpro.databinding.HiveListFragmentBinding;
import com.beekeeperpro.databinding.InspectionListFragmentBinding;
import com.beekeeperpro.ui.hive.HiveListAdapter;
import com.beekeeperpro.ui.hive.HiveListViewModel;
import com.beekeeperpro.ui.menu.EditMenuProvider;

public class InspectionListFragment extends Fragment {

    EditMenuProvider editMenu;
    private InspectionListViewModel viewModel;

    public InspectionListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        InspectionListFragmentBinding binding = InspectionListFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(InspectionListViewModel.class);
        viewModel.setHive(getArguments().getParcelable("hive"));

        InspectionListAdapter adapter = new InspectionListAdapter();
        adapter.getOnClickedItem().observe(getViewLifecycleOwner(), hive -> {
            //todo make something
        });
        adapter.getOnDeleteItem().observe(getViewLifecycleOwner(), hive -> viewModel.delete(hive));
        adapter.getOnAddInspectionItem().observe(getViewLifecycleOwner(), hive -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            Bundle args = new Bundle();
            args.putParcelable("hive", hive);
            navController.navigate(R.id.action_hives_list_fragment_to_addInspection, args);
        });
        binding.inspectionList.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), adapter::setInspectionList);

        editMenu = getEditMenu(adapter);
        return binding.getRoot();
    }

    @NonNull
    private EditMenuProvider getEditMenu(InspectionListAdapter adapter) {
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
        requireActivity().findViewById(R.id.fab).setOnClickListener(v->{
            Bundle arg = new Bundle();
            arg.putParcelable("hive", viewModel.getHive());
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_inspection_list_fragment_to_add_Inspection_fragment, arg);
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