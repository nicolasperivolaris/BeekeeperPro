package com.beekeeperpro.ui.gallery;

import android.content.Context;
import android.os.Environment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<File[]> images;

    public GalleryViewModel() {
        images = new MutableLiveData<>();
    }

    public LiveData<File[]> getImages() {
        return images;
    }

    public void update(Context context) {
        images.setValue(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles());
    }
}