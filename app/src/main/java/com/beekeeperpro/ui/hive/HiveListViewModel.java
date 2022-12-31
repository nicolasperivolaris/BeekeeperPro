package com.beekeeperpro.ui.hive;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.util.List;

public class HiveListViewModel extends ConnectedViewModel<List<Hive>> {
    private int apiaryId;

    public void setApiaryId(int id){
        apiaryId = id;
    }

    public int getApiaryId() {
        return apiaryId;
    }

    @Override
    protected Result getFromSource() {
        return dataSource.getHives(apiaryId);
    }
}