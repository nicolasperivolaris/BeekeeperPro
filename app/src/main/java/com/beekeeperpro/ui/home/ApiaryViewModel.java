package com.beekeeperpro.ui.home;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.BPViewModel;

import java.util.List;

public class ApiaryViewModel extends BPViewModel<List<Hive>> {
    private int apiaryId;

    public void setApiaryId(int id){
        apiaryId = id;
    }

    @Override
    protected Result getDataFromSource() {
        return dataSource.getHives(apiaryId);
    }
}