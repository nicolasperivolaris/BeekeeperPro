package com.beekeeperpro.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.beekeeperpro.data.model.Apiary;

public class AddApiaryViewModel  extends ViewModel {
    private Apiary current;
    private MutableLiveData<String> error;

    public AddApiaryViewModel() {
        current = new Apiary();
        error = new MutableLiveData<>();
    }

    public Apiary getCurrent() {
        return current;
    }

    public void setCurrent(Apiary current) {
        this.current = current;
    }

    public LiveData<String> getError() {
        return error;
    }

    public boolean save() {
        if(current.getName().trim().equals("")){
            error.postValue("Name can't be empty.");
            return false;
        }
        return true;
    }
}
