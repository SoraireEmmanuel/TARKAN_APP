package com.example.organizadortareaskanban.ui.eMail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.adaptadores.BusquedaTareasAdapter;
import com.example.organizadortareaskanban.entidades.Tarea;
import com.example.organizadortareaskanban.ui.tareas.TareasActivity;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Properties;

public class EMailActivity extends AppCompatActivity {
TextView titulo, destinatario, contenido;
String correo, contrasenia;
Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_mail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    destinatario=(EditText)findViewById(R.id.correoMail);
    titulo=(EditText)findViewById(R.id.tituloMail);
    contenido=(EditText)findViewById(R.id.textoMail);
    correo="tarkanutn@gmail.com";
    contrasenia="safa";
    }

    public void enviarMail(){

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

/*
        try {
            session= Session.getDefaultInstance(props, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(correo,contrasenia);
                }
            });


        }catch (Exception e){

        }


*/

        Intent mail= new Intent(Intent.ACTION_SEND);
        mail.setData(Uri.parse("mailto"));
        mail.setType("text/plain");
        mail.putExtra(Intent.EXTRA_EMAIL, destinatario.getText());
        mail.putExtra(Intent.EXTRA_SUBJECT, titulo.getText());
        mail.putExtra(Intent.EXTRA_TEXT, contenido.getText());
        startActivity(mail.createChooser(mail,"Send Email"));
    }


    public void enviar(){
        String enviarcorreo = destinatario.getText().toString();
        String enviarasunto = titulo.getText().toString();
        String enviarmensaje = contenido.getText().toString();

        // Defino mi Intent y hago uso del objeto ACTION_SEND
        Intent intent = new Intent(Intent.ACTION_SEND);

        // Defino los Strings Email, Asunto y Mensaje con la función putExtra
        intent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { enviarcorreo });
        intent.putExtra(Intent.EXTRA_SUBJECT, enviarasunto);
        intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);

        // Establezco el tipo de Intent
        intent.setType("message/rfc822");

        // Lanzo el selector de cliente de Correo
        startActivity(
                Intent
                        .createChooser(intent,
                                "Elije un cliente de Correo:"));
    }



    @Override public  boolean onCreateOptionsMenu(Menu mimenu){
        getMenuInflater().inflate(R.menu.enviar, mimenu);

        return true;
    }
    //opciones de los items del menu
    @Override public  boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();
        if(id==R.id.enviarMail) {
            enviar();
            return true;
        }

        return super.onOptionsItemSelected(opcion_menu);

    }
    }

