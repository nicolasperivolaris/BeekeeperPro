package com.beekeeperpro.ui.inspection;

import androidx.lifecycle.ViewModel;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.Inspection;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.util.List;

public class InspectionListViewModel extends ConnectedViewModel<List> {
    private Hive hive;

    public InspectionListViewModel() {
        super(List.class);
    }

    public Hive getHive() {
        return hive;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    @Override
    protected Result getFromSource() {
        return dataSource.getInspections(hive);
    }

    public void delete(Inspection inspection) {
        update(() -> dataSource.delete(inspection));
        update();
    }
}