package com.example.organizadortareaskanban;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;
import com.example.organizadortareaskanban.ui.proyectos.BuscarProyectoActivity;
import com.example.organizadortareaskanban.ui.proyectos.CrearProyecto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayInputStream;

public class MainActivity extends AppCompatActivity {

    String usuariosharepreference;
    byte[] image;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, CrearProyecto.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //con esto generamos el usuario en el header del menu-------------------------------
        View hView = navigationView.getHeaderView(0);
        TextView usuario = (TextView) hView.findViewById(R.id.usuarioMenuLateral);
        TextView nombre  = (TextView) hView.findViewById(R.id.nombreMenuLateral);
        ImageView img = (ImageView) hView.findViewById(R.id.imageMenuLateral);
        cargarPreferencias();

        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(getApplicationContext(), "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();
        String selection = Utilidades.CAMPO_USUARIO + " = ?";
        String [] proyeccion = {Utilidades.CAMPO_USUARIO,Utilidades.CAMPO_CONTRASENIA,Utilidades.CAMPO_FOTO};
        String[] selectionArg = {usuariosharepreference};
        try{
            Cursor c = db.query(Utilidades.TABLA_USUARIOS, proyeccion,
                    selection , selectionArg, null, null, null);
            c.moveToFirst();
            usuario.setText(c.getString(0));
            nombre.setText(c.getString(1));
            image = c.getBlob(2);

            ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
            Bitmap imgbitmap = BitmapFactory.decodeStream(imageStream);
            img.setImageBitmap(imgbitmap);

            //        Toast.makeText(getApplicationContext(),"El usuario  existe", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            //       Toast.makeText(getApplicationContext(),"El usuario no existe", Toast.LENGTH_SHORT).show();
        }
/*
        }*/
        //----------------------------------------------------------------------------------

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void cargarPreferencias() {
        SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference = preferences.getString("usuario","No existe la informacion");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public  boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();
        if(id==R.id.buscarproyecto) {
            Intent intent=new Intent(this, BuscarProyectoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(opcion_menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea cerrar la aplicacion?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });
        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }


    public void cancelar() {
        finish();
    }

}
