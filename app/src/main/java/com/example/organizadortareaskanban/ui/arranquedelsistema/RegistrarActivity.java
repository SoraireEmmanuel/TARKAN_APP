package com.example.organizadortareaskanban.ui.arranquedelsistema;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.organizadortareaskanban.MainActivity;
import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class RegistrarActivity extends AppCompatActivity {

    ImageView imagen;
    Spinner spiner;
    String path;
    Button registro;
    EditText campoUsuario;

    EditText campoNombres;
    EditText campoTelefono;
    String spinerText;
    Bitmap imgByte;
    Boolean bandera=false;

    //componentes de la conccion a la BBDD
    ProgressDialog progreso;

    //variables para la carga de foto
    private final String CARPETA_RAIZ="misImagenes/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    final int COD_SELECCIONADA=10;
    final int COD_FOTO=20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //REGISTRO LA IMAGEN DEL FORMULARIO EN LA CONSTANTE imagen
        imagen=(ImageView)findViewById(R.id.imagenId);


        //REGISTRO EL SPINNER EN LA VARIABLE spiner PARA MANIPULAR SI VISUALIZACION
        spiner=(Spinner)findViewById(R.id.spinner);
        String [] opciones = {"Elija su Equipo...","Equipo1","Equipo2","Equipo3","Equipo4", "Equipo5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, opciones);
        spiner.setPrompt("Seleccione su equipo");
        spiner.setAdapter(adapter);

        //REGISTRO EL RESTO DE LOS CAMPOS PARA SU CARGA EN LA BASE DE DATOS
        campoUsuario=(EditText)findViewById(R.id.usuario);
        campoNombres =(EditText)findViewById(R.id.nombres);
        campoTelefono = (EditText)findViewById(R.id.telefono);

        scripCargarUsuarios();

    }

    public void registar(){
        if(bandera) {registrarUsuario();
        Intent iniciar = new Intent(this, MainActivity.class);
        //usamos shared preferences para almacenar el usuario
        SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = campoUsuario.getText().toString();
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("usuario",usuario);
        editor.commit();
        //iniciar.putExtra("usuario", campoUsuario.getText().toString());
        startActivity(iniciar);
        }
        else{
            Toast.makeText(getApplicationContext(),"Se tiene que cargar una imagen para registrarse",Toast.LENGTH_SHORT).show();}
    }

    private void registrarUsuario() {
        // transforma el bit map en arreglo de byte
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imgByte.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] img = bos.toByteArray();
        ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
        SQLiteDatabase db=conex.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_USUARIO,campoUsuario.getText().toString());
        values.put(Utilidades.CAMPO_CONTRASENIA,campoNombres.getText().toString());
        values.put(Utilidades.CAMPO_EQUIPO,(String) spiner.getSelectedItem());
        values.put(Utilidades.CAMPO_TELEFONO,campoTelefono.getText().toString());
        values.put(Utilidades.CAMPO_FOTO, img);

        Long idResultante=db.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USUARIO,values);

        Toast.makeText(getApplicationContext(),"DNI registro:"+idResultante,Toast.LENGTH_SHORT).show();
    }

    //Método para crear un nombre único de cada fotografia version sdk mayor o igual a M
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Método para tomar foto y crear el archivo
    static final int REQUEST_TAKE_PHOTO = 1;
    public void tomarFoto2() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    //Método para mostrar vista previa en un imageview de la foto tomada
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONADA:
                    Uri miPath = data.getData();
                    imagen.setImageURI(miPath);
                    try {
                        imgByte = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                        bandera = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imgByte = (Bitmap) extras.get("data");
                    imagen.setImageBitmap(imgByte);
                    bandera = true;
                    break;


            }
        }
    }

    //metodos para la carga de foto para versiones de android 5.1 Lollipop o menores
    public void cargarImagen(View view) {
        cargar();
    }

    private void cargar(){
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpcion=new AlertDialog.Builder(RegistrarActivity.this);
        alertOpcion.setTitle("Seleccione una Opcion");
        alertOpcion.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(opciones[i].equals("Tomar Foto")) {
                    if (ContextCompat.checkSelfPermission(RegistrarActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegistrarActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegistrarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);

                    } else {
                        tomarFoto2();
                    }

                }

/*
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        tomarFoto2();
                    }else
                     {
                        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                            tomarFoto2();
                        }
                        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
                            cargarDialogoRecomendacion();
                        }
                    }
                }*/
                else{
                    if(opciones[i].equals("Cargar Imagen")){
                        Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicacion"),COD_SELECCIONADA);
                    }
                    else{
                        dialog.dismiss();
                    }
                }
            }


            private void cargarDialogoRecomendacion() {
            AlertDialog.Builder dialog= new AlertDialog.Builder(RegistrarActivity.this);
            dialog.setTitle("Permisos Desactivados");
            dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la APP ");
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(RegistrarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);

                        }
                }
            });
            dialog.show();
            }
        });
        alertOpcion.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1000) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                tomarFoto2();
            }
        }
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
            && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                tomarFoto2();
            }

        }
    }

    private void tomarFoto() {
        String nombreImagen="";
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada= fileImagen.exists();
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }
        if(isCreada==true) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }
        path=Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        File imag=new File(path);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imag));
        startActivityForResult(intent,COD_FOTO);
    }
*/
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONADA:
                    Uri miPath=data.getData();
                    imagen.setImageURI(miPath);
                    try {
                        imgByte = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                        bandera=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento","Path: "+path);
                        }
                    });
                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);
                    imgByte= bitmap;
                    bandera=true;
                    break;
            }
        }
    }

*/

    @Override public  boolean onCreateOptionsMenu(Menu mimenu){
        getMenuInflater().inflate(R.menu.crearobjeto, mimenu);
        return true;
    }
    //opciones de los items del menu
    @Override public  boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();
        if(id==R.id.okMenu) {
            registar();
            return true;
        }
        return super.onOptionsItemSelected(opcion_menu);
    }



    private void scripCargarUsuarios() {
        /*
        * Crea 5 usuarios para la prueba
        *
         */
        // transforma la img del drawable en bit map y el bit map en arreglo de byte
        Bitmap baronbitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.imgperfilprueba);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        baronbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imagenbaron = bos.toByteArray();

        Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.mujerperfilimagen);
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos2);
        byte[] imagenmujer = bos2.toByteArray();

        ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
        SQLiteDatabase db=conex.getWritableDatabase();

        //usuario 1
        ContentValues values1=new ContentValues();
        values1.put(Utilidades.CAMPO_USUARIO,"JuanOliv@gmail.com");
        values1.put(Utilidades.CAMPO_CONTRASENIA,"Juan");
        values1.put(Utilidades.CAMPO_EQUIPO,"Equipo 1");
        values1.put(Utilidades.CAMPO_TELEFONO,"221-3245238");
        values1.put(Utilidades.CAMPO_FOTO, imagenbaron);
        Long idResultante1=db.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USUARIO,values1);

        //usuario 2
        ContentValues values2=new ContentValues();
        values2.put(Utilidades.CAMPO_USUARIO,"MariaCas@gmail.com");
        values2.put(Utilidades.CAMPO_CONTRASENIA,"Maria Casco");
        values2.put(Utilidades.CAMPO_EQUIPO,"Equipo 1");
        values2.put(Utilidades.CAMPO_TELEFONO,campoTelefono.getText().toString());
        values2.put(Utilidades.CAMPO_FOTO, imagenmujer);
        Long idResultante2=db.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USUARIO,values2);

        //usuario 3
        ContentValues values3=new ContentValues();
        values3.put(Utilidades.CAMPO_USUARIO,"LuciaVit@gmail.com");
        values3.put(Utilidades.CAMPO_CONTRASENIA,"Lucia Vitali");
        values3.put(Utilidades.CAMPO_EQUIPO,"Equipo 2");
        values3.put(Utilidades.CAMPO_TELEFONO,"221-7634107");
        values3.put(Utilidades.CAMPO_FOTO, imagenmujer);
        Long idResultante3=db.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USUARIO,values3);

        //usuario 4
        ContentValues values4=new ContentValues();
        values4.put(Utilidades.CAMPO_USUARIO,"FranVilla@gmail.com");
        values4.put(Utilidades.CAMPO_CONTRASENIA,"Francisco Villanueva");
        values4.put(Utilidades.CAMPO_EQUIPO,"Equipo 2");
        values4.put(Utilidades.CAMPO_TELEFONO,"221-7843260");
        values4.put(Utilidades.CAMPO_FOTO, imagenbaron);
        Long idResultante4=db.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USUARIO,values4);
        //usuario 5
        ContentValues values5=new ContentValues();
        values5.put(Utilidades.CAMPO_USUARIO,"JoseFinaP@gmail.com");
        values5.put(Utilidades.CAMPO_CONTRASENIA,"JoseFina Perez");
        values5.put(Utilidades.CAMPO_EQUIPO,"Equipo 3");
        values5.put(Utilidades.CAMPO_TELEFONO,"221-4329832");
        values5.put(Utilidades.CAMPO_FOTO, imagenmujer);
        Long idResultante5=db.insert(Utilidades.TABLA_USUARIOS,Utilidades.CAMPO_USUARIO,values5);

        /*
        * Crea 2 Proyectos para la prueba
        *
        */
        //proyecto 1
        ContentValues values6=new ContentValues();
        values6.put(Utilidades.CAMPO_ID_PROYECTO,"1");
        values6.put(Utilidades.CAMPO_NOMBRE_PROYECTO,"Proyecto Booking");
        values6.put(Utilidades.CAMPO_FECHA_PROYECTO,"01/04/2020");
        Long idResultante6=db.insert(Utilidades.TABLA_PROYECTOS,Utilidades.CAMPO_ID_PROYECTO,values6);

        //proyecto 2
        ContentValues values7=new ContentValues();
        values7.put(Utilidades.CAMPO_ID_PROYECTO,"2");
        values7.put(Utilidades.CAMPO_NOMBRE_PROYECTO,"Proyecto TARKAN");
        values7.put(Utilidades.CAMPO_FECHA_PROYECTO,"22/04/2020");
        Long idResultante7=db.insert(Utilidades.TABLA_PROYECTOS,Utilidades.CAMPO_ID_PROYECTO,values7);

        /*
        *Creacion relaciones entre Usuario y proyecto
        */
        ContentValues values8=new ContentValues();
        values8.put(Utilidades.CAMPO_ID_PROYECTO,"1");
        values8.put(Utilidades.CAMPO_ID_USUARIO,"JuanOliv@gmail.com");
        Long idResultante8=db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values8);

        ContentValues values9=new ContentValues();
        values9.put(Utilidades.CAMPO_ID_PROYECTO,"1");
        values9.put(Utilidades.CAMPO_ID_USUARIO,"MariaCas@gmail.com");
        Long idResultante9=db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values9);

             ContentValues values10=new ContentValues();
        values10.put(Utilidades.CAMPO_ID_PROYECTO,"1");
        values10.put(Utilidades.CAMPO_ID_USUARIO,"LuciaVit@gmail.com");
        Long idResultante10=db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values10);

        ContentValues values11=new ContentValues();
        values11.put(Utilidades.CAMPO_ID_PROYECTO,"2");
        values11.put(Utilidades.CAMPO_ID_USUARIO,"LuciaVit@gmail.com");
        Long idResultante11=db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values11);

        ContentValues values12=new ContentValues();
        values12.put(Utilidades.CAMPO_ID_PROYECTO,"2");
        values12.put(Utilidades.CAMPO_ID_USUARIO,"FranVilla@gmail.com");
        Long idResultante12=db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values12);

        ContentValues values13=new ContentValues();
        values13.put(Utilidades.CAMPO_ID_PROYECTO,"2");
        values13.put(Utilidades.CAMPO_ID_USUARIO,"JoseFinaP@gmail.com");
        Long idResultante13=db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values13);

        /*
        *Creacion de Tareas
         */
        //tarea 1
        ContentValues values14=new ContentValues();
        values14.put(Utilidades.CAMPO_ID,1);
        values14.put(Utilidades.CAMPO_DESCRIPCION,"Hacer que las cars de la pagina tengan opacidad 0.5 cuando se haga hover en ellas");
        values14.put(Utilidades.CAMPO_FECHACREACION,"04/04/2020");
        values14.put(Utilidades.CAMPO_PRIORIDAD,"Baja");
        values14.put(Utilidades.CAMPO_TITULO,"Feature 1");
        values14.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values14.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values14.put(Utilidades.CAMPO_CREADOPOR,"JuanOliv@gmail.com");
        values14.put(Utilidades.CAMPO_ESTADO,"Iniciada");
        values14.put(Utilidades.CAMPO_PROYECTO,"1");
        Long idResultante14=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values14);
        //tarea 2
        ContentValues values15=new ContentValues();
        values15.put(Utilidades.CAMPO_ID,2);
        values15.put(Utilidades.CAMPO_DESCRIPCION,"Implementar un sistema de puntaje en la calificacion de libros");
        values15.put(Utilidades.CAMPO_FECHACREACION,"22/04/2020");
        values15.put(Utilidades.CAMPO_PRIORIDAD,"Alta");
        values15.put(Utilidades.CAMPO_TITULO,"Feature 2");
        values15.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values15.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values15.put(Utilidades.CAMPO_CREADOPOR,"JuanOliv@gmail.com");
        values15.put(Utilidades.CAMPO_ESTADO,"Ejecutada");
        values15.put(Utilidades.CAMPO_PROYECTO,"1");
        Long idResultante15=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values15);


        //tarea 3
        ContentValues values16=new ContentValues();
        values16.put(Utilidades.CAMPO_ID,3);
        values16.put(Utilidades.CAMPO_DESCRIPCION,"Visualizar en el detalle de los libros el pais en el que fue editadoo");
        values16.put(Utilidades.CAMPO_FECHACREACION,"21/04/2020");
        values16.put(Utilidades.CAMPO_PRIORIDAD,"Media");
        values16.put(Utilidades.CAMPO_TITULO,"Feature 3");
        values16.put(Utilidades.CAMPO_REALIZADOPOR,"Nonne");
        values16.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values16.put(Utilidades.CAMPO_CREADOPOR,"MariaCas@gmail.com");
        values16.put(Utilidades.CAMPO_ESTADO,"Ejecutada");
        values16.put(Utilidades.CAMPO_PROYECTO,"1");
        Long idResultante16=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values16);

        //tarea 4
        ContentValues values17=new ContentValues();
        values17.put(Utilidades.CAMPO_ID,4);
        values17.put(Utilidades.CAMPO_DESCRIPCION,"Desarrollar una opcion para que el buscador filtre por año y editorial");
        values17.put(Utilidades.CAMPO_FECHACREACION,"12/04/2020");
        values17.put(Utilidades.CAMPO_PRIORIDAD,"Alta");
        values17.put(Utilidades.CAMPO_TITULO,"Feature 4");
        values17.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values17.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values17.put(Utilidades.CAMPO_CREADOPOR,"MariaCas@gmail.com");
        values17.put(Utilidades.CAMPO_ESTADO,"Esperada");
        values17.put(Utilidades.CAMPO_PROYECTO,"1");
        Long idResultante17=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values17);

        //tarea 5
        ContentValues values18=new ContentValues();
        values18.put(Utilidades.CAMPO_ID,5);
        values18.put(Utilidades.CAMPO_DESCRIPCION,"Desarrollar una seccion de comentarios para que los letores puedan dejar reseñas de los libros");
        values18.put(Utilidades.CAMPO_FECHACREACION,"01/04/2020");
        values18.put(Utilidades.CAMPO_PRIORIDAD,"Alta");
        values18.put(Utilidades.CAMPO_TITULO,"Feature 5");
        values18.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values18.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values18.put(Utilidades.CAMPO_CREADOPOR,"LuciaVit@gmail.com");
        values18.put(Utilidades.CAMPO_ESTADO,"Revisada");
        values18.put(Utilidades.CAMPO_PROYECTO,"1");
        Long idResultante18=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values18);

        //tarea 6
        ContentValues values19=new ContentValues();
        values19.put(Utilidades.CAMPO_ID,6);
        values19.put(Utilidades.CAMPO_DESCRIPCION,"Crear un splash con el nombre de los desarrolladores y con un efecto para el logo y el nombre de la app");
        values19.put(Utilidades.CAMPO_FECHACREACION,"27/05/2020");
        values19.put(Utilidades.CAMPO_PRIORIDAD,"Baja");
        values19.put(Utilidades.CAMPO_TITULO,"Splash de la app");
        values19.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values19.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values19.put(Utilidades.CAMPO_CREADOPOR,"LuciaVit@gmail.com");
        values19.put(Utilidades.CAMPO_ESTADO,"Iniciada");
        values19.put(Utilidades.CAMPO_PROYECTO,"2");
        Long idResultante19=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values19);

        //tarea 7
        ContentValues values20=new ContentValues();
        values20.put(Utilidades.CAMPO_ID,7);
        values20.put(Utilidades.CAMPO_DESCRIPCION,"Desarrollar un layaut para la creacion de una nueva tarea");
        values20.put(Utilidades.CAMPO_FECHACREACION,"22/04/2020");
        values20.put(Utilidades.CAMPO_PRIORIDAD,"Media");
        values20.put(Utilidades.CAMPO_TITULO,"Layaout NuevaTarea");
        values20.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values20.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values20.put(Utilidades.CAMPO_CREADOPOR,"FranVilla@gmail.com");
        values20.put(Utilidades.CAMPO_ESTADO,"Ejecutada");
        values20.put(Utilidades.CAMPO_PROYECTO,"2");
        Long idResultante20=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values20);
        //tarea 8
        ContentValues values21=new ContentValues();
        values21.put(Utilidades.CAMPO_ID,8);
        values21.put(Utilidades.CAMPO_DESCRIPCION,"Resolver el error que surge en el loguin, ya que si fallas el loguin en el primer intento no lo podes volver hacer");
        values21.put(Utilidades.CAMPO_FECHACREACION,"30/05/2020");
        values21.put(Utilidades.CAMPO_PRIORIDAD,"Alta");
        values21.put(Utilidades.CAMPO_TITULO,"Bug Loguin");
        values21.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values21.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values21.put(Utilidades.CAMPO_CREADOPOR,"FranVilla@gmail.com");
        values21.put(Utilidades.CAMPO_ESTADO,"Esperada");
        values21.put(Utilidades.CAMPO_PROYECTO,"2");
        Long idResultante21=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values21);

        //tarea 9
        ContentValues values22=new ContentValues();
        values22.put(Utilidades.CAMPO_ID,9);
        values22.put(Utilidades.CAMPO_DESCRIPCION,"Resolver la fluidez con la que corre la app cuando se carga imagenes de mucha resolucion");
        values22.put(Utilidades.CAMPO_FECHACREACION,"14/05/2020");
        values22.put(Utilidades.CAMPO_PRIORIDAD,"Alta");
        values22.put(Utilidades.CAMPO_TITULO,"Bug Fluidez");
        values22.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values22.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values22.put(Utilidades.CAMPO_CREADOPOR,"JoseFinaP@gmail.com");
        values22.put(Utilidades.CAMPO_ESTADO,"Esperada");
        values22.put(Utilidades.CAMPO_PROYECTO,"2");
        Long idResultante22=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values22);

        //tarea 10
        ContentValues values23=new ContentValues();
        values23.put(Utilidades.CAMPO_ID,10);
        values23.put(Utilidades.CAMPO_DESCRIPCION,"Desarrollar busqueda en el toolbar del layout tareas");
        values23.put(Utilidades.CAMPO_FECHACREACION,"19/05/2020");
        values23.put(Utilidades.CAMPO_PRIORIDAD,"Alta");
        values23.put(Utilidades.CAMPO_TITULO,"Funcionalidad Busqueda");
        values23.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values23.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values23.put(Utilidades.CAMPO_CREADOPOR,"JoseFinaP@gmail.com");
        values23.put(Utilidades.CAMPO_ESTADO,"Esperada");
        values23.put(Utilidades.CAMPO_PROYECTO,"2");
        Long idResultante23=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values23);
        //tarea 11
        ContentValues values24=new ContentValues();
        values24.put(Utilidades.CAMPO_ID,11);
        values24.put(Utilidades.CAMPO_DESCRIPCION,"agragar una funcionalidad que prmita poner alarmas a las tareas de prioridad alta");
        values24.put(Utilidades.CAMPO_FECHACREACION,"19/05/2020");
        values24.put(Utilidades.CAMPO_PRIORIDAD,"Media");
        values24.put(Utilidades.CAMPO_TITULO,"Funcionalidad Alarma");
        values24.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values24.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values24.put(Utilidades.CAMPO_CREADOPOR,"JoseFinaP@gmail.com");
        values24.put(Utilidades.CAMPO_ESTADO,"Revisada");
        values24.put(Utilidades.CAMPO_PROYECTO,"2");
        Long idResultante24=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values24);

        //tarea 12
        ContentValues values25=new ContentValues();
        values25.put(Utilidades.CAMPO_ID,12);
        values25.put(Utilidades.CAMPO_DESCRIPCION,"Crear una funcionalidad que permita que los usuarios puedan loguear en la pagina web para asi ganar funcionalidades ocultas");
        values25.put(Utilidades.CAMPO_FECHACREACION,"19/05/2020");
        values25.put(Utilidades.CAMPO_PRIORIDAD,"Alta");
        values25.put(Utilidades.CAMPO_TITULO,"Featuree 6");
        values25.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values25.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values25.put(Utilidades.CAMPO_CREADOPOR,"JoseFinaP@gmail.com");
        values25.put(Utilidades.CAMPO_ESTADO,"Revisada");
        values25.put(Utilidades.CAMPO_PROYECTO,"1");
        Long idResultante25=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values25);

        //tarea 13
        ContentValues values26=new ContentValues();
        values26.put(Utilidades.CAMPO_ID,13);
        values26.put(Utilidades.CAMPO_DESCRIPCION,"agragar una funcionalidad que prmita poner alarmas a las tareas de prioridad alta");
        values26.put(Utilidades.CAMPO_FECHACREACION,"19/05/2020");
        values26.put(Utilidades.CAMPO_PRIORIDAD,"Media");
        values26.put(Utilidades.CAMPO_TITULO,"Funcionalidad Nuevaa Alarma");
        values26.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values26.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values26.put(Utilidades.CAMPO_CREADOPOR,"JoseFinaP@gmail.com");
        values26.put(Utilidades.CAMPO_ESTADO,"Finalizada");
        values26.put(Utilidades.CAMPO_PROYECTO,"2");
        Long idResultante26=db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values26);

    }
}
