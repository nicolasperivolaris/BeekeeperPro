package com.beekeeperpro.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.beekeeperpro.MainActivity;
import com.beekeeperpro.data.ApiaryRepository;
import com.beekeeperpro.data.model.Apiary;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final ApiaryRepository repository;

    public HomeViewModel() {
        repository = ApiaryRepository.getInstance(MainActivity.dataSource);
    }

    public LiveData<List<Apiary>> getApiaries() {
        return repository.getApiaries();
    }

    public void update(){
        repository.update();
    }
}