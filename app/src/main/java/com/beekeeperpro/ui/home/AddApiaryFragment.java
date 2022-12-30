package com.beekeeperpro.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;

public class AddApiaryFragment extends Fragment {

    AddApiaryViewModel viewModel;
    public AddApiaryFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddApiaryViewModel.class);
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        viewModel.getError().observe(getViewLifecycleOwner(), s -> Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show());
        return inflater.inflate(R.layout.fragment_add_apiary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addSaveButton();
    }

    private void addSaveButton() {
        MenuItem saveBt = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.save_button);
        saveBt.setVisible(true);
        saveBt.setOnMenuItemClickListener(item -> {
            loadViewModel();
            if(viewModel.save()){
                NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                Bundle args = new Bundle();
                args.putParcelable("apiary", viewModel.getCurrent());
                nav.navigate(R.id.action_addApiaryFragment_to_nav_home, args);
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromViewModel();
    }

    private void loadFromViewModel() {
        Apiary apiary = viewModel.getCurrent();
        ((TextView)getView().findViewById(R.id.apiaryName)).setText(apiary.getName());
        ((TextView)getView().findViewById(R.id.apiaryLocation)).setText(apiary.getLocation());
        ((TextView)getView().findViewById(R.id.apiaryLat)).setText(apiary.getCoordinate().getLatitude()+"");
        ((TextView)getView().findViewById(R.id.apiaryLong)).setText(apiary.getCoordinate().getLongitude()+"");
    }

    @Override
    public void onPause() {
        super.onPause();
        loadViewModel();
    }

    private void loadViewModel() {
        Apiary apiary = viewModel.getCurrent();
        apiary.setName(((TextView)getView().findViewById(R.id.apiaryName)).getText().toString());
        apiary.setLocation(((TextView)getView().findViewById(R.id.apiaryLocation)).getText().toString());
        apiary.setCoordinate(Double.parseDouble(((TextView)getView().findViewById(R.id.apiaryLat)).getText().toString()),
                Double.parseDouble(((TextView)getView().findViewById(R.id.apiaryLat)).getText().toString()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        ((Toolbar)getActivity().findViewById(R.id.toolbar)).findViewById(R.id.save_button).setVisibility(View.GONE);

    }
}