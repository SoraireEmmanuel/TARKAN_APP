package com.example.organizadortareaskanban.adaptadores;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.entidades.Tarea;

import java.util.ArrayList;

public class BusquedaTareasAdapter extends RecyclerView.Adapter<BusquedaTareasAdapter.ViewHolder>{
    ArrayList<Tarea> listDatos;
    final private BusquedaTareasAdapter.ListClick mOn2ClickListener;

    public BusquedaTareasAdapter(ArrayList<Tarea> listDatos, BusquedaTareasAdapter.ListClick listener) {
        this.listDatos = listDatos;
        mOn2ClickListener=listener;
    }

    public interface ListClick{
        void onListClick(Integer id, String nombre);
    }

    @Override
    public BusquedaTareasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.busqueda_tarea_recycler, parent, false);
        return new BusquedaTareasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusquedaTareasAdapter.ViewHolder holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tituloTareaoView, fechaTareaView, prioridadTareaView, estadoTareaView;
        Integer id;
        String nombre;
        @SuppressLint("CutPasteId")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTareaoView=(TextView) itemView.findViewById(R.id.tituloBusquedaTarea);
            fechaTareaView =(TextView) itemView.findViewById(R.id.fechaBusquedaTarea);
            prioridadTareaView=(TextView)itemView.findViewById(R.id.prioridadBusquedaTarea);
            estadoTareaView=(TextView)itemView.findViewById(R.id.estadoBusquedaTarea);
            itemView.setOnClickListener(this);
        }

        public void asignarDatos(Tarea tarea) {
            tituloTareaoView.setText(tarea.getTitulo());
            fechaTareaView.setText(tarea.getFechaCreacion());
            prioridadTareaView.setText(tarea.getPrioridad());
            estadoTareaView.setText(tarea.getEstado());
            nombre=tarea.getTitulo();
            id=tarea.getId();
        }
        @Override
        public void onClick(View v) {
            mOn2ClickListener.onListClick( id , nombre );
        }
    }
}
