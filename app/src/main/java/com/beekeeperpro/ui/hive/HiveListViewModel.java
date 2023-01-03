package com.beekeeperpro.ui.hive;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.util.List;

import kotlin.jvm.internal.TypeReference;

public class HiveListViewModel extends ConnectedViewModel<List> {
    private Apiary apiary;

    public HiveListViewModel() {
        super(List.class);
    }

    public void setApiary(Apiary apiary){
        this.apiary = apiary;
    }

    public Apiary getApiary() {
        return apiary;
    }

    @Override
    protected Result getFromSource() {
        return dataSource.getHives(apiary);
    }

    public void delete(Hive hive) {
        update(() -> dataSource.delete(hive));
        update();
    }
}