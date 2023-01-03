package com.beekeeperpro.ui.apiary;

import com.beekeeperpro.data.LoginRepository;
import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.util.List;

public class ApiaryListViewModel extends ConnectedViewModel<List> {

    public ApiaryListViewModel() {
        super(List.class);
    }

    @Override
    protected Result getFromSource() {
        return dataSource.getApiaries(LoginRepository.getLoggedInUser());
    }

    void delete(Apiary apiary){
        update(() ->dataSource.delete(apiary));
        update();
    }
}