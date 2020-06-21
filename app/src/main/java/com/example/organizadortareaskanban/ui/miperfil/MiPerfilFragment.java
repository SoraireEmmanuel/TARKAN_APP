package com.example.organizadortareaskanban.ui.miperfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;

import java.io.ByteArrayInputStream;

public class MiPerfilFragment extends Fragment {

    ImageView imagen;
    TextView nombre, usuario, equipo, telefono ;

    private MiPerfilViewModel miPerfilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miPerfilViewModel =
                ViewModelProviders.of(this).get(MiPerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_miperfil, container, false);

        imagen=root.findViewById(R.id.miPerfilImagen);
        nombre=root.findViewById(R.id.nombreMiPerfil);
        usuario=root.findViewById(R.id.usuarioMiPerfil);
        equipo=root.findViewById(R.id.equipoMiPerfil);
        telefono=root.findViewById(R.id.telefonoMiPerfil);
        verperfil();

        miPerfilViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private void verperfil() {

        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(getContext(), "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuariosharepreference = preferences.getString("usuario", "No existe la informacion");

        String [] proyeccion = {Utilidades.CAMPO_CONTRASENIA,Utilidades.CAMPO_EQUIPO,
                Utilidades.CAMPO_TELEFONO,Utilidades.CAMPO_FOTO}; //repornar los proyectos
        String selection = Utilidades.CAMPO_USUARIO + " = ?"; //
        String[] selectionArg = {usuariosharepreference}; //se va a buscar por usuario logueado

        Cursor c = db.query(Utilidades.TABLA_USUARIOS, proyeccion,
                selection, selectionArg, null, null, null);
        c.moveToFirst();
        usuario.setText(usuariosharepreference);
        nombre.setText(c.getString(0));
        telefono.setText(c.getString(2));
        equipo.setText(c.getString(1));
        byte[] img = c.getBlob(3);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(img);
        Bitmap imgbitmap = BitmapFactory.decodeStream(imageStream);
        imagen.setImageBitmap(imgbitmap);

    }
}
