package com.beekeeperpro.ui.home;

import com.beekeeperpro.data.LoginRepository;
import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.util.List;

public class ApiaryListViewModel extends ConnectedViewModel<List<Apiary>> {

    @Override
    protected Result getFromSource() {
        return dataSource.getApiaries(LoginRepository.getLoggedInUser());
    }
}