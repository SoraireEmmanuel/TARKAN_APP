package com.example.organizadortareaskanban.ui.informacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.organizadortareaskanban.R;

public class InformacionFragment extends Fragment {

    private InformacionViewModel informacionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        informacionViewModel =
                ViewModelProviders.of(this).get(InformacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_informacion, container, false);

        informacionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}
