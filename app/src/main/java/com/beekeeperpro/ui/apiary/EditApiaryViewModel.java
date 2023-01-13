package com.beekeeperpro.ui.apiary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.ui.ConnectedViewModel;

public class EditApiaryViewModel extends ConnectedViewModel<Apiary> {
    private final MutableLiveData<String> validationError;

    public EditApiaryViewModel() {
        super(Apiary.class);
        validationError = new MutableLiveData<>();
    }

    public void initWith(Apiary apiary){
        data.setValue(apiary);
    }

    public LiveData<String> getValidationError() {
        return validationError;
    }

    /**
     * Save in db if no validation error
     *
     * @return true if no validation error
     */
    public boolean save() {
        if (data.getValue().getName().trim().equals("")) {
            validationError.postValue("Name can't be empty.");
            return false;
        }
        update(data.getValue());
        return true;
    }
}