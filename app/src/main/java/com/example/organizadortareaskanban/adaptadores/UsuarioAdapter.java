package com.example.organizadortareaskanban.adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.entidades.Tarea;
import com.example.organizadortareaskanban.entidades.Usuario;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {
    ArrayList<Usuario> listDatos;
    final private UsuarioAdapter.ListClick mOnClickListener;

    public UsuarioAdapter(ArrayList<Usuario> listDatos, UsuarioAdapter.ListClick listener) {
        this.listDatos = listDatos;
        mOnClickListener=listener;
    }

    public interface ListClick{
        void onListClick(Integer id);
    }

    @Override
    public UsuarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuario_recycler, parent, false);
        return new UsuarioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.ViewHolder holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView usuarioView, telefono, nombre;
        ImageView foto;
        Integer tel;
        byte[] fotoByte;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.nombreUsuarioActivity);
            usuarioView=(TextView) itemView.findViewById(R.id.usuarioUsuarioActivity);
            telefono =(TextView) itemView.findViewById(R.id.telefonoUsuarioActivity);
            foto = (ImageView) itemView.findViewById(R.id.fotoUsuarioActivity);
            itemView.setOnClickListener(this);
        }

        public void asignarDatos(Usuario usuario) {
            usuarioView.setText(usuario.getUsuario());
            nombre.setText(usuario.getNombre());
            telefono.setText(usuario.getTelefono());
            tel=usuario.getTelefono();
            fotoByte=usuario.getFoto();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(fotoByte);
            Bitmap image = BitmapFactory.decodeStream(imageStream);
            foto.setImageBitmap(image);
        }
        @Override
        public void onClick(View v) {
            mOnClickListener.onListClick( tel );
        }
    }

}
