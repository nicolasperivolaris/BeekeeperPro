package com.beekeeperpro.ui.hive;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.sql.SQLException;
import java.util.List;

public class HiveListViewModel extends ConnectedViewModel<List> {
    private Apiary apiary;
    public boolean cameFromFindHive = false;

    public HiveListViewModel() {
        super(List.class);
    }

    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
    }

    public Apiary getApiary() {
        return apiary;
    }

    @Override
    public Result getFromSource() {
        try {
            return new Result.Success<>(apiary.selectHives());
        } catch (SQLException e) {
            return new Result.Error(e);
        }
    }

    public void delete(Hive hive) {
        execute(() -> {
            try {
                Result r = new Result.Success<>(hive.delete());
                select();
                return r;
            } catch (SQLException e) {
                e.printStackTrace();
                return new Result.Error(e);
            }
        });
    }
}