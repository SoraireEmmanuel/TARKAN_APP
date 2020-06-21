package com.example.organizadortareaskanban.ui.tareas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.w3c.dom.Text;

public class VerTareaActivity extends AppCompatActivity {
    TextView titulo, descripcion, estado, prioridad, fecha, creador, desarrolador, testeador;
    private Integer tareaSharePreference;
    Button revisar, termine, realizar;
    private String usuariosharepreference;

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


}
