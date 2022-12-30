package com.beekeeperpro.ui.apiary;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.util.List;

public class ApiaryViewModel extends ConnectedViewModel<List<Hive>> {
    private int apiaryId;

    public void setApiaryId(int id){
        apiaryId = id;
    }

    @Override
    protected Result getFromSource() {
        return dataSource.getHives(apiaryId);
    }
}