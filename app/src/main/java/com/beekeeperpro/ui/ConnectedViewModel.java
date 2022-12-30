package com.beekeeperpro.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.beekeeperpro.MainActivity;
import com.beekeeperpro.data.DataSource;
import com.beekeeperpro.data.Result;

public abstract class ConnectedViewModel<T> extends ViewModel {
    protected final DataSource dataSource;
    protected final MutableLiveData<T> data;
    protected final MutableLiveData<Result.Error> error;

    protected ConnectedViewModel() {
        this.dataSource = MainActivity.dataSource;
        data = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }
    public LiveData<T> getData(){
        return data;
    }

    public LiveData<Result.Error> getErrors(){
        return error;
    }
    public void update(){
        request(() -> getFromSource());
    }

    public void insert(T data){
        request(() -> insertIntoSource(data));
    }

    public void request(Requester requester){
        Thread t = new Thread(() ->{
            //todo remove the while
            boolean ok = false;
            Result result = null;
            while(!ok) {
                try {
                    result = requester.request();
                    ok = true;
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            if(result instanceof Result.Success)
                data.postValue(((Result.Success<T>)result).getData());
            else{
                System.err.println(result.toString());
                error.postValue((Result.Error) result);
            }
        });
        t.start();
    }

    protected Result getFromSource(){
        return new Result.Error(new UnsupportedOperationException());
    }

    protected Result insertIntoSource(T data){
        return new Result.Error(new UnsupportedOperationException());
    }

    private interface Requester{
        Result request();
    }
}