package com.beekeeperpro.ui.hive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.ui.ConnectedViewModel;

public class AddHiveViewModel extends ConnectedViewModel<Hive> {
    private final MutableLiveData<String> validationError;
    private Apiary apiary;

    public AddHiveViewModel() {
        super(Hive.class);
        validationError = new MutableLiveData<>();
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }

    public boolean save() {
        data.getValue().setApiary(apiary);
        if (data.getValue().getName().trim().equals("")) {
            validationError.postValue("Name can't be empty.");
            return false;
        }
        insert(data.getValue());
        return true;
    }

    public Apiary getApiary() {
        return apiary;
    }

    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
    }
}