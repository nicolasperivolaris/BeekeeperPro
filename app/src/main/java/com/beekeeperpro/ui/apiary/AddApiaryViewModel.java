package com.beekeeperpro.ui.apiary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.ConnectedViewModel;

public class AddApiaryViewModel  extends ConnectedViewModel<Apiary> {

    private MutableLiveData<String> validationError;

    public AddApiaryViewModel() {
        validationError = new MutableLiveData<>();
        data.setValue(new Apiary());
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }

    public boolean save() {
        if(data.getValue().getName().trim().equals("")){
            validationError.postValue("Name can't be empty.");
            return false;
        }
        insert(data.getValue());
        return true;
    }

    @Override
    public MutableLiveData<Apiary> getData() {
        return data;
    }

    @Override
    protected Result<Boolean> insertIntoSource(Apiary data) {
        return dataSource.insert(data);
    }
}
