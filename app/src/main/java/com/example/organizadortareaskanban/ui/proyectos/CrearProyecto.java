package com.example.organizadortareaskanban.ui.proyectos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizadortareaskanban.MainActivity;
import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearProyecto extends AppCompatActivity {
    
    EditText nombreProyecto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proyecto);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        nombreProyecto=(EditText)findViewById(R.id.nombreNuevoProyecto);

    }

    private void crearProyecto() {
        ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
        SQLiteDatabase db=conex.getWritableDatabase();
        //definimos la fecha del sistema
        Date date = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", date.getTime());
        SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMM, yyyy");
        String newDateStr = postFormater.format(date);

        String [] proyeccion ={Utilidades.CAMPO_ID_PROYECTO};
        Cursor c = db.query(Utilidades.TABLA_PROYECTOS, proyeccion,
                null, null, null, null, null);
        Integer cantidadDeRegistro=c.getCount();
        cantidadDeRegistro++;

        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_ID_PROYECTO,String.valueOf(cantidadDeRegistro));
        values.put(Utilidades.CAMPO_NOMBRE_PROYECTO,nombreProyecto.getText().toString());
        values.put(Utilidades.CAMPO_FECHA_PROYECTO,newDateStr);
        db.insert(Utilidades.TABLA_PROYECTOS,Utilidades.CAMPO_ID_PROYECTO,values);

        //recuperamos el usuario logueado
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuariosharepreference = preferences.getString("usuario", "No existe la informacion");
        //creamos la relacion del usuario con el proyecto
        ContentValues values8=new ContentValues();
        values8.put(Utilidades.CAMPO_ID_PROYECTO,String.valueOf(cantidadDeRegistro));
        values8.put(Utilidades.CAMPO_ID_USUARIO,usuariosharepreference);
        //values8.put(Utilidades.CAMPO_ID_USUARIO,"LuciaVit@gmail.com");
        db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values8);

        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);

    }
        


    @Override public  boolean onCreateOptionsMenu(Menu mimenu){
        getMenuInflater().inflate(R.menu.crearobjeto, mimenu);
        return true;
    }
    //opciones de los items del menu
    @Override public  boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();
        if(id==R.id.okMenu) {
            crearProyecto();
            return true;
        }
        return super.onOptionsItemSelected(opcion_menu);
    }

    
}
