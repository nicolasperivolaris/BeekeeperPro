package com.beekeeperpro.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.beekeeperpro.MainActivity;
import com.beekeeperpro.data.Result;
import com.beekeeperpro.data.model.ApiaryEntity;
import com.beekeeperpro.data.model.DataSource;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public abstract class ConnectedViewModel<T> extends ViewModel {
    protected final DataSource dataSource;
    protected final MutableLiveData<T> data;
    protected final MutableLiveData<Boolean> done;
    protected final MutableLiveData<Result.Error> error;

    protected ConnectedViewModel(Class<T> c) {
        MutableLiveData<T> dataTemp;
        this.dataSource = MainActivity.dataSource;
        done = new MutableLiveData<>();
        error = new MutableLiveData<>();
        try {
            dataTemp = new MutableLiveData<>((T) c.getConstructor().newInstance());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("No default constructor for class " + getClass().getSuperclass().getTypeParameters()[0]);
            System.err.println(e);
            dataTemp = new MutableLiveData<>();
        }
        data = dataTemp;
    }

    @NonNull
    public LiveData<T> getData() {
        return data;
    }

    public LiveData<Result.Error> getErrors() {
        return error;
    }

    public void select() {
        execute(this::getFromSource);
    }

    public void insert(ApiaryEntity data) {
        execute(() -> insertIntoSource(data));
    }

    public void update(ApiaryEntity data) {
        execute(() -> updateInSource(data));
    }

    public void delete(ApiaryEntity data) {
        execute(() -> deleteFromSource(data));
    }

    protected void execute(Requester requester) {
        Thread t = new Thread(() -> {
            //todo remove the while
            boolean ok = false;
            Object result = null;
            while (!ok) {
                try {
                    result = requester.request();
                    ok = true;
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            if (result instanceof Result.Success) {
                Object o = ((Result.Success) result).getData();
                if (o instanceof Boolean) done.postValue((boolean) o);
                else data.postValue((T) o);
            } else {
                System.err.println(result.toString());
                error.postValue((Result.Error) result);
            }
        });
        t.start();
    }

    /**
     * Don't use it, overload it.
     *
     * @return
     */
    protected Result getFromSource() {
        throw new UnsupportedOperationException();
    }

    /**
     * Don't use it, overload it.
     *
     * @return
     */
    protected Result insertIntoSource(ApiaryEntity data) {
        try {
            return new Result.Success<>(data.insert());
        } catch (SQLException e) {
            return new Result.Error(e);
        }
    }

    /**
     * Don't use it, overload it.
     *
     * @return
     */
    protected Result updateInSource(ApiaryEntity data) {
        try {
            return new Result.Success<>(data.update());
        } catch (SQLException e) {
            return new Result.Error(e);
        }
    }

    /**
     * Don't use it, overload it.
     *
     * @return
     */
    protected Result deleteFromSource(ApiaryEntity data) {
        try {
            return new Result.Success<>(data.delete());
        } catch (SQLException e) {
            return new Result.Error(e);
        }
    }

    public LiveData<Boolean> getDone() {
        return done;
    }

    public interface Requester {
        Object request();
    }
}