package com.beekeeperpro.ui.hive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.beekeeperpro.R;
import com.beekeeperpro.databinding.FragmentHiveListBinding;

public class HiveListFragment extends Fragment {

    private HiveListViewModel viewModel;

    public HiveListFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HiveListViewModel.class);
        viewModel.setApiaryId(getArguments().getInt("id"));
        viewModel.setApiaryId(getArguments().getInt("id"));
        FragmentHiveListBinding binding = FragmentHiveListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        HiveListAdapter adapter = new HiveListAdapter();
        adapter.getClickedId().observe(getViewLifecycleOwner(), integer -> {});
        binding.hiveList.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), adapter::setHiveList);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.fab).setOnClickListener(v->{
            Bundle arg = new Bundle();
            arg.putInt("apiaryId", viewModel.getApiaryId());
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.action_hives_list_to_add_hive_fragment, arg);
        });
        viewModel.update();
    }

}