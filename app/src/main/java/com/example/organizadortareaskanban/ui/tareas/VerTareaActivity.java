package com.example.organizadortareaskanban.ui.tareas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizadortareaskanban.MainActivity;
import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;
import com.example.organizadortareaskanban.entidades.Tarea;
import com.example.organizadortareaskanban.ui.proyectos.BuscarProyectoActivity;
import com.example.organizadortareaskanban.ui.usuarios.UsuariosActivity;

import org.w3c.dom.Text;

public class VerTareaActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10;
    TextView titulo, descripcion, estado, prioridad, fecha, creador, desarrolador, testeador;
    private Integer tareaSharePreference;
    Button revisar, termine, realizar;
    private String usuariosharepreference;
    private String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tarea);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        titulo=(TextView)findViewById(R.id.verTareaTitulo);
        descripcion=(TextView)findViewById(R.id.verTareaDescripcion);
        estado=(TextView)findViewById(R.id.verTareaEstado);
        prioridad=(TextView)findViewById(R.id.verTareaPrioridad);
        fecha=(TextView)findViewById(R.id.verTareaFecha);
        creador=(TextView)findViewById(R.id.verTareaCreador);
        desarrolador=(TextView)findViewById(R.id.verTareaDesarrollador);
        testeador=(TextView)findViewById(R.id.verTareaTesteador);
        consultaTarea();

        realizar=(Button)findViewById(R.id.realizar);
        revisar=(Button)findViewById(R.id.revisar);
        termine=(Button)findViewById(R.id.termine);
        realizar.setVisibility(View.GONE);
        revisar.setVisibility(View.GONE);
        termine.setVisibility(View.GONE);
        inicializarBotones();
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference = preferences.getString("usuario","No existe la informacion");

    }

    private void inicializarBotones() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference = preferences.getString("usuario","No existe la informacion");
        switch (estado.getText().toString()){
            case "Iniciada":
                realizar.setVisibility(View.VISIBLE);
                break;
            case "Ejecutada":
               // Toast.makeText(this, "No tiene Proyectos" , +usuariosharepreferenceToast.LENGTH_SHORT).show();
                if(usuariosharepreference.contains(desarrolador.getText().toString())){
                    termine.setVisibility(View.VISIBLE);


                }
                break;
            case "Esperada":
                revisar.setVisibility(View.VISIBLE);
                break;
            case "Revisada":
                if(usuariosharepreference.contains(testeador.getText().toString())){
                    termine.setVisibility(View.VISIBLE);
                }
                break;
            case "Finalizada":
                break;
        }
    }

    private void consultaTarea() {
        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(this, "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();

        SharedPreferences preferences2 = getSharedPreferences("tarea", Context.MODE_PRIVATE);
        tareaSharePreference = preferences2.getInt("id",0);


        String [] proyeccion = {Utilidades.CAMPO_TITULO, Utilidades.CAMPO_FECHACREACION,Utilidades.CAMPO_PRIORIDAD,Utilidades.CAMPO_ESTADO,
                Utilidades.CAMPO_CREADOPOR,Utilidades.CAMPO_REVISADOPOR,Utilidades.CAMPO_REALIZADOPOR,Utilidades.CAMPO_DESCRIPCION};
        String selection = Utilidades.CAMPO_ID + " = ?"; //
        String[] selectionArg = {String.valueOf(tareaSharePreference)}; //se va a buscar tareas del proycto en sharepreferenc

        Cursor c = db.query(Utilidades.TABLA_TAREAS, proyeccion,
                selection, selectionArg, null, null, null);

        c.moveToFirst();
        titulo.setText(c.getString(0));
        descripcion.setText(c.getString(7));
        estado.setText(c.getString(3));
        prioridad.setText(c.getString(2));
        fecha.setText(c.getString(1));
        creador.setText(c.getString(4));
        desarrolador.setText(c.getString(6));
        testeador.setText(c.getString(5));
    }
    public void realizar(View view){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea realizar esta Tarea?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(VerTareaActivity.this, "bd_usuario", null,1);
                SQLiteDatabase db = conex.getWritableDatabase();
                SharedPreferences preferences2 = getSharedPreferences("tarea", Context.MODE_PRIVATE);
                tareaSharePreference = preferences2.getInt("id",0);
                ContentValues values=new ContentValues();
                values.put(Utilidades.CAMPO_REALIZADOPOR, usuariosharepreference);
                values.put(Utilidades.CAMPO_ESTADO,"Ejecutada");
                String[] parametro = {String.valueOf(tareaSharePreference)};
                db.update(Utilidades.TABLA_TAREAS,values,Utilidades.CAMPO_ID+"=?",parametro);
                db.close();
                Intent intent=new Intent(VerTareaActivity.this, TareasActivity.class);
                startActivity(intent);
            }
        });
        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }

    public void revisar(View view){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea testear esta Tarea?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(VerTareaActivity.this, "bd_usuario", null,1);
                SQLiteDatabase db = conex.getWritableDatabase();
                SharedPreferences preferences2 = getSharedPreferences("tarea", Context.MODE_PRIVATE);
                tareaSharePreference = preferences2.getInt("id",0);
                ContentValues values=new ContentValues();
                values.put(Utilidades.CAMPO_REVISADOPOR, usuariosharepreference);
                values.put(Utilidades.CAMPO_ESTADO,"Revisada");
                String[] parametro = {String.valueOf(tareaSharePreference)};
                db.update(Utilidades.TABLA_TAREAS,values,Utilidades.CAMPO_ID+"=?",parametro);
                db.close();
                Intent intent=new Intent(VerTareaActivity.this, TareasActivity.class);
                startActivity(intent);
            }
        });
        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }


    public void termine(View view){
        if(estado.getText().toString().contains("Ejecutada")) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("¿Seguro que termino y deja la tarea para revision?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    final ConexionSQLiteHelper conex = new ConexionSQLiteHelper(VerTareaActivity.this, "bd_usuario", null, 1);
                    SQLiteDatabase db = conex.getWritableDatabase();
                    SharedPreferences preferences2 = getSharedPreferences("tarea", Context.MODE_PRIVATE);
                    tareaSharePreference = preferences2.getInt("id", 0);
                    ContentValues values = new ContentValues();
                    values.put(Utilidades.CAMPO_ESTADO, "Esperada");
                    String[] parametro = {String.valueOf(tareaSharePreference)};
                    db.update(Utilidades.TABLA_TAREAS, values, Utilidades.CAMPO_ID + "=?", parametro);
                    db.close();
                    Intent intent = new Intent(VerTareaActivity.this, TareasActivity.class);
                    startActivity(intent);
                }
            });
            dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }
        else {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("¿Seguro que termino el test y deja la tarea finalizada?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    final ConexionSQLiteHelper conex = new ConexionSQLiteHelper(VerTareaActivity.this, "bd_usuario", null, 1);
                    SQLiteDatabase db = conex.getWritableDatabase();
                    SharedPreferences preferences2 = getSharedPreferences("tarea", Context.MODE_PRIVATE);
                    tareaSharePreference = preferences2.getInt("id", 0);
                    ContentValues values = new ContentValues();
                    values.put(Utilidades.CAMPO_ESTADO, "Finalizada");
                    String[] parametro = {String.valueOf(tareaSharePreference)};
                    db.update(Utilidades.TABLA_TAREAS, values, Utilidades.CAMPO_ID + "=?", parametro);
                    db.close();
                    Intent intent = new Intent(VerTareaActivity.this, TareasActivity.class);
                    startActivity(intent);
                }
            });
            dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();

        }
        }

public void creador(View view) {
    final ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
    SQLiteDatabase db = conex.getReadableDatabase();

    String[] proyeccion2 = {Utilidades.CAMPO_TELEFONO};
    String selection2 = Utilidades.CAMPO_USUARIO + " = ?";
    String[] selectionArg2 = {creador.getText().toString()};
    try {

        Cursor c = db.query(Utilidades.TABLA_USUARIOS, proyeccion2,
                selection2, selectionArg2, null, null, null);
        c.moveToFirst();
        llamar(c.getInt(0),creador.getText().toString());

    } catch (Exception e) {

    }
}

public void revisador(View view){
        final ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
    SQLiteDatabase db = conex.getReadableDatabase();

    String[] proyeccion2 = {Utilidades.CAMPO_TELEFONO};
    String selection2 = Utilidades.CAMPO_USUARIO + " = ?";
    String[] selectionArg2 = {testeador.getText().toString()};
try {
    Cursor c = db.query(Utilidades.TABLA_USUARIOS, proyeccion2,
            selection2, selectionArg2, null, null, null);
    c.moveToFirst();
    llamar(c.getInt(0),testeador.getText().toString());
}catch (Exception e){

}
}
public void realizador(View view) {
    final ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
    SQLiteDatabase db = conex.getReadableDatabase();

    String[] proyeccion2 = {Utilidades.CAMPO_TELEFONO};
    String selection2 = Utilidades.CAMPO_USUARIO + " = ?";
    String[] selectionArg2 = {desarrolador.getText().toString()};
    try {
        Cursor c = db.query(Utilidades.TABLA_USUARIOS, proyeccion2,
                selection2, selectionArg2, null, null, null);
        c.moveToFirst();
        llamar(c.getInt(0),desarrolador.getText().toString());
    } catch (Exception e) {

    }
}


    public void llamar(final Integer id, final String destinatario) {
        telefono=String.valueOf(id);
        final CharSequence[] opciones = {"Llamar", "Enviar Mail", "Cancelar"};
        final AlertDialog.Builder alertOpcion = new AlertDialog.Builder(VerTareaActivity.this);
        alertOpcion.setTitle("Seleccione una Opcion");
        alertOpcion.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Llamar")) {
                    if (ActivityCompat.checkSelfPermission
                            (VerTareaActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(VerTareaActivity.this, new String[]
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
