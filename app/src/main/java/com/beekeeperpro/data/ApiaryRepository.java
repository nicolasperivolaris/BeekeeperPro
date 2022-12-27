package com.beekeeperpro.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeperpro.data.model.Apiary;

import java.util.List;

public class ApiaryRepository{
    private static volatile ApiaryRepository instance;
    private final DataSource dataSource;
    private final MutableLiveData<List<Apiary>> apiaries;

    private ApiaryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        apiaries = new MutableLiveData<>();
    }
    public static ApiaryRepository getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new ApiaryRepository(dataSource);
        }
        return instance;
    }

    public void update(){
        Thread t = new Thread(() ->{
            //todo remove the while
            boolean ok = false;
            Result result = null;
            while(!ok) {
                try {
                    result = dataSource.getApiaries(LoginRepository.getLoggedInUser());
                    ok = true;
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            if(result instanceof Result.Success)
                apiaries.postValue(((Result.Success<List<Apiary>>)result).getData());
        });
        t.start();
    }

    public LiveData<List<Apiary>> getApiaries(){
        return apiaries;
    }
}
