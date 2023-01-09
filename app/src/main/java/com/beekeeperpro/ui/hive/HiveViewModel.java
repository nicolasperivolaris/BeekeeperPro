package com.beekeeperpro.ui.hive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.Inspection;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.sql.SQLException;

public class HiveViewModel extends ConnectedViewModel<Hive> {
    private final MutableLiveData<String> validationError;
    private int hiveId;

    public HiveViewModel() {
        super(Hive.class);
        validationError = new MutableLiveData<>();
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }

    public boolean save() {
        if (data.getValue().getName().trim().equals("")) {
            validationError.postValue("Name can't be empty.");
            return false;
        }
        update(data.getValue());
        return true;
    }

    public void initWith(Hive hive) {
        hiveId = hive.getId();
        select();
    }

    @Override
    protected Result getFromSource() {
        try {
            Hive hive = Hive.select(hiveId);
            hive.selectInspections();
            return new Result.Success<>(hive);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(new Exception("Error while getting inspection list"));
        }
    }
}