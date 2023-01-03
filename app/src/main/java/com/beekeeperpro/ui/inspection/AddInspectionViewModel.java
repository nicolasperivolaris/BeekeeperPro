package com.beekeeperpro.ui.inspection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.beekeeperpro.data.model.Apiary;
import com.beekeeperpro.data.model.Hive;
import com.beekeeperpro.data.model.Inspection;
import com.beekeeperpro.ui.ConnectedViewModel;

public class AddInspectionViewModel extends ConnectedViewModel<Inspection> {

    private final Hive hive;
    private MutableLiveData<String> validationError;

    public AddInspectionViewModel(Hive hive) {
        super();
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

    public LiveData<String> getValidationError() {
        return validationError;
    }
}