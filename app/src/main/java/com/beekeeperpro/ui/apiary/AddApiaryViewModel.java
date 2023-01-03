package com.beekeeperpro.ui.apiary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.ConnectedViewModel;
import com.google.android.gms.common.api.Api;

public class AddApiaryViewModel  extends ConnectedViewModel<Apiary> {

    private MutableLiveData<String> validationError;

    public AddApiaryViewModel() {
        super(Apiary.class);
        validationError = new MutableLiveData<>();
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }

    /**
     * Save in db if no validation error
     * @return true if no validation error
     */
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
    protected Result insertIntoSource(Apiary data) {
        return dataSource.insert(data);
    }
}
