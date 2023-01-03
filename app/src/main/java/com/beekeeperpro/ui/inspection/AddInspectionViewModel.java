package com.beekeeperpro.ui.inspection;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.Inspection;
import com.beekeeperpro.ui.ConnectedViewModel;

import java.sql.SQLException;

public class AddInspectionViewModel extends ConnectedViewModel<Inspection> {

    private final Hive hive;
    private MutableLiveData<String> validationError;

    public AddInspectionViewModel(Hive hive) {
        super(Inspection.class);
        validationError = new MutableLiveData<>();
        this.hive = hive;
    }

    public boolean save() {
        data.getValue().setHive(hive);
        if(data.getValue().getInspectionDate() == null){
            validationError.postValue("Choose an inspection date");
            return false;
        }
        insert(data.getValue());
        return true;
    }

    protected Result insertIntoSource(Inspection inspection){
        try {
            return dataSource.insert(inspection);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result.Error(e);
        }
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }
}