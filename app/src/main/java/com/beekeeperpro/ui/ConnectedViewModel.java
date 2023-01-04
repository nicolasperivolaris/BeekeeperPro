package com.beekeeperpro.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.beekeeperpro.MainActivity;
import com.beekeeperpro.data.DataSource;
import com.beekeeperpro.data.Result;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ConnectedViewModel<T> extends ViewModel {
    protected final DataSource dataSource;
    protected final MutableLiveData<T> data;
    protected final MutableLiveData<Result.Error> error;

    protected ConnectedViewModel(Class<T> c) {
        MutableLiveData<T> dataTemp;
        this.dataSource = MainActivity.dataSource;

        error = new MutableLiveData<>();
        try{
            dataTemp = new MutableLiveData<>((T) c.getConstructor().newInstance());
        }catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e){
            System.err.println("No default constructor for class " + getClass().getSuperclass().getTypeParameters()[0]);
            System.err.println(e);
            dataTemp = new MutableLiveData<>();
        }
        data = dataTemp;
    }
    @NonNull
    public LiveData<T> getData(){
        return data;
    }

    public LiveData<Result.Error> getErrors(){
        return error;
    }
    public void update(){
        update(() -> getFromSource());
    }

    public void insert(T data){
        update(() -> insertIntoSource(data));
    }

    protected void update(Requester requester){
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

    protected interface Requester{
        Result request();
    }
}