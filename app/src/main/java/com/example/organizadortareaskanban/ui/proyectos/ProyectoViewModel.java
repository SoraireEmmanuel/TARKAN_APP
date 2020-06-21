package com.example.organizadortareaskanban.ui.proyectos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProyectoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProyectoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}