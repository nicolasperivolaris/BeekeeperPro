package com.beekeeperpro.ui.apiary;

import com.beekeeperpro.data.LoginRepository;
import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.sql.SQLException;
import java.util.List;

public class ApiaryListViewModel extends ConnectedViewModel<List> {

    public ApiaryListViewModel() {
        super(List.class);
    }

    @Override
    public Result getFromSource() {
        try {
            try {
                return new Result.Success<>(LoginRepository.getLoggedInUser().getApiaries());
            } catch (SQLException e) {
                return new Result.Success<>(LoginRepository.getLoggedInUser().getApiaries());
            }
        } catch (SQLException e) {
            return new Result.Error(e);
        }
    }

    public void delete(Apiary hive) {
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