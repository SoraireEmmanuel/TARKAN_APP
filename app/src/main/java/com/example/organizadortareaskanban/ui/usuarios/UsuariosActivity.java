package com.example.organizadortareaskanban.ui.usuarios;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.adaptadores.ProyectosAdapter;
import com.example.organizadortareaskanban.adaptadores.TareasAdapter;
import com.example.organizadortareaskanban.adaptadores.UsuarioAdapter;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;
import com.example.organizadortareaskanban.entidades.Proyecto;
import com.example.organizadortareaskanban.entidades.Tarea;
import com.example.organizadortareaskanban.entidades.Usuario;
import com.example.organizadortareaskanban.ui.arranquedelsistema.RegistrarActivity;
import com.example.organizadortareaskanban.ui.eMail.EMailActivity;

import java.util.ArrayList;

public class UsuariosActivity extends AppCompatActivity implements UsuarioAdapter.ListClick {
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10;
    RecyclerView recycler;
    String telefono;
    ArrayList<Usuario> usuariosDelProyecto;
    private String proyectoSharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        recycler = findViewById(R.id.recycleUsuario);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        consultarUsuarioProyecto();
        UsuarioAdapter adapter = new UsuarioAdapter(usuariosDelProyecto, this);
        recycler.setAdapter(adapter);


    }

    private void consultarUsuarioProyecto() {
        usuariosDelProyecto = new ArrayList<Usuario>();
        final ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
        SQLiteDatabase db = conex.getReadableDatabase();
        SharedPreferences preferences = getSharedPreferences("proyecto", Context.MODE_PRIVATE);
        proyectoSharePreference = preferences.getString("id", "No existe la informacion");

        String[] proyeccion = {Utilidades.CAMPO_USUARIO, Utilidades.CAMPO_CONTRASENIA, Utilidades.CAMPO_TELEFONO,
                Utilidades.CAMPO_FOTO};
        String selection = Utilidades.CAMPO_USUARIO + " = ?"; //

        String[] proyeccion2 = {Utilidades.CAMPO_ID_USUARIO};
        String selection2 = Utilidades.CAMPO_ID_PROYECTO + " = ?";
        String[] selectionArg2 = {proyectoSharePreference};

        try {
            //consulta los usuarios que perteneces al proycto en ejecucion
            Cursor c = db.query(Utilidades.TABLA_RELACION_PROYECTO, proyeccion2,
                    selection2, selectionArg2, null, null, null);
            c.moveToFirst();
            //consulta la informacion de los usuarios de la consulta anterior
            String[] selectionArg = {c.getString(0)};
            Cursor c2 = db.query(Utilidades.TABLA_USUARIOS, proyeccion,
                    selection, selectionArg, null, null, null);
            c2.moveToFirst();
            usuariosDelProyecto.add(new Usuario(c2.getString(0), c2.getString(1),
                    c2.getInt(2), c2.getBlob(3)));
            while (c.moveToNext()) {
                String[] selectionArg3 = {c.getString(0)};
                Cursor c3 = db.query(Utilidades.TABLA_USUARIOS, proyeccion,
                        selection, selectionArg3, null, null, null);
                c3.moveToFirst();
                usuariosDelProyecto.add(new Usuario(c.getString(0), c3.getString(1),
                        c3.getInt(2), c3.getBlob(3)));

            }

        } catch (Exception e) {

        }

    }

    @Override
    public void onListClick(final Integer id, final String destinatario) {
        telefono=String.valueOf(id);
        final CharSequence[] opciones = {"Llamar", "Enviar Mail", "Cancelar"};
        final AlertDialog.Builder alertOpcion = new AlertDialog.Builder(UsuariosActivity.this);
        alertOpcion.setTitle("Seleccione una Opcion");
        alertOpcion.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Llamar")) {
                    if (ActivityCompat.checkSelfPermission
                            (UsuariosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UsuariosActivity.this, new String[]
                                {Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telefono));
                        startActivity(intent);
                    }
                } else {
                    if (opciones[i].equals("Enviar Mail")) {
                        String enviarcorreo = destinatario;

                        // Defino mi Intent y hago uso del objeto ACTION_SEND
                        Intent intent = new Intent(Intent.ACTION_SEND);

                        // Defino los Strings Email, Asunto y Mensaje con la función putExtra
                        intent.putExtra(Intent.EXTRA_EMAIL,
                                new String[] { enviarcorreo });

                        // Establezco el tipo de Intent
                        intent.setType("message/rfc822");

                        // Lanzo el selector de cliente de Correo
                        startActivity(
                                Intent
                                        .createChooser(intent,
                                                "Elije un cliente de Correo:"));
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        alertOpcion.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO, procede a realizar lo que tienes que hacer
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telefono));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Permiso consdido :",Toast.LENGTH_SHORT).show();

                } else {
                    // PERMISO DENEGADO
                }
                return;
            }
        }
    }



}
