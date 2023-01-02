package com.beekeeperpro.ui.hive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.ConnectedViewModel;

public class AddHiveViewModel extends ConnectedViewModel<Hive> {
    private MutableLiveData<String> validationError;
    private int currentApiary;

    public AddHiveViewModel() {
        validationError = new MutableLiveData<>();
        data.setValue(new Hive());
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }

    public boolean save() {
        data.getValue().setApiary(new Apiary(currentApiary, "", "", 0));
        if(data.getValue().getName().trim().equals("")){
            validationError.postValue("Name can't be empty.");
            return false;
        }
        insert(data.getValue());
        return true;
    }

    @Override
    public MutableLiveData<Hive> getData() {
        return data;
    }

    @Override
    protected Result insertIntoSource(Hive hive) {
        return dataSource.insert(hive);
    }

    public int getCurrentApiary() {
        return currentApiary;
    }

    public void setCurrentApiary(int currentApiary) {
        this.currentApiary = currentApiary;
    }
}