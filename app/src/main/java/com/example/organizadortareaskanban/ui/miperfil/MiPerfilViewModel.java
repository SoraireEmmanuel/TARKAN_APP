package com.example.organizadortareaskanban.ui.miperfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MiPerfilViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MiPerfilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}