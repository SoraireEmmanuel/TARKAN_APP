package com.example.organizadortareaskanban.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.entidades.Proyecto;
import com.example.organizadortareaskanban.entidades.Tarea;

import java.util.ArrayList;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.ViewHolder> {
    ArrayList<Tarea> listDatos;
    final private TareasAdapter.ListClick mOnClickListener;

    public TareasAdapter(ArrayList<Tarea> listDatos, TareasAdapter.ListClick listener) {
        this.listDatos = listDatos;
        mOnClickListener=listener;
    }

    public interface ListClick{
        void onListClick(Integer id);
    }

    @Override
    public TareasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_recycler, parent, false);
        return new TareasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareasAdapter.ViewHolder holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tituloTareaoView, fechaTareaView, prioridadTareaView;
        Integer id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTareaoView=(TextView) itemView.findViewById(R.id.tituloTareaTablero);
            fechaTareaView =(TextView) itemView.findViewById(R.id.fechaTareaTablero);
            prioridadTareaView=(TextView)itemView.findViewById(R.id.prioridadTablero);
            itemView.setOnClickListener(this);
        }

        public void asignarDatos(Tarea tarea) {
            tituloTareaoView.setText(tarea.getTitulo());
            fechaTareaView.setText(tarea.getFechaCreacion());
            prioridadTareaView.setText(tarea.getPrioridad());
            id=tarea.getId();
        }
        @Override
        public void onClick(View v) {
            mOnClickListener.onListClick( id );
        }
    }

}
