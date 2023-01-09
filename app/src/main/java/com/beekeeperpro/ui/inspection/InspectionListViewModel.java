package com.beekeeperpro.ui.inspection;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.Inspection;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.sql.SQLException;
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
    public Result getFromSource() {
        try {
            return new Result.Success<>(hive.selectInspections());
        } catch (SQLException e) {
            return new Result.Error(e);
        }
    }

    public void delete(Inspection inspection) {
        execute(() -> {
            try {
                Result r = new Result.Success<>(inspection.delete());
                select();
                return r;
            } catch (SQLException e) {
                e.printStackTrace();
                return new Result.Error(e);
            }
        });
    }
}