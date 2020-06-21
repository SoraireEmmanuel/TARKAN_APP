package com.example.organizadortareaskanban.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.entidades.Proyecto;

import java.util.ArrayList;
import java.util.Date;

public class ProyectosAdapter extends RecyclerView.Adapter<ProyectosAdapter.ViewHolder> {
    ArrayList<Proyecto> listDatos;
    final private ListClick mOnClickListener;

    public ProyectosAdapter(ArrayList<Proyecto> listDatos, ListClick listener) {
        this.listDatos = listDatos;
        mOnClickListener=listener;
    }

    public interface ListClick{
        void onListClick(String id, String nombre);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proyecto_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nombreProyectoView, fechaProyectoView;
        String id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProyectoView=(TextView) itemView.findViewById(R.id.proyectoId);
            fechaProyectoView =(TextView) itemView.findViewById(R.id.fechaProyecto);
            itemView.setOnClickListener(this);
        }

        public void asignarDatos(Proyecto proyecto) {
            nombreProyectoView.setText(proyecto.getNombre());
            fechaProyectoView.setText(proyecto.getFecha());
            id=proyecto.getId();
        }
        @Override
        public void onClick(View v) {
            String nombre = nombreProyectoView.getText().toString();
            mOnClickListener.onListClick( id , nombre);
        }
    }

}
