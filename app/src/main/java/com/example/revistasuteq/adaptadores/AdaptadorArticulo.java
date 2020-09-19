package com.example.revistasuteq.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.revistasuteq.R;
import com.example.revistasuteq.objetos.Articulo;

import java.util.ArrayList;

public class AdaptadorArticulo extends RecyclerView.Adapter<AdaptadorArticulo.MyViewHolder>
        implements View.OnClickListener  {

    private Context mContext;

    private View.OnClickListener listener;
    private ArrayList<Articulo> mLista;


    public TextView lblSeccion;
    public TextView lblabstract;
    public TextView lblTitulo;
    public TextView lblFechaPublicacion;


    public AdaptadorArticulo(Context context, ArrayList<Articulo> lista) {
        mContext = context;
        mLista=lista;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
        {
            listener.onClick(view);
        }
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        int view_type;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblSeccion=(TextView) itemView.findViewById(R.id.txtPublicaId);
            lblabstract=(TextView) itemView.findViewById(R.id.txtabstract);
            lblTitulo=(TextView) itemView.findViewById(R.id.txtTitulo);
            lblFechaPublicacion=(TextView) itemView.findViewById(R.id.txtFecha);

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_articulo,null,false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            lblSeccion.setText(mLista.get(position).getSection());
            lblTitulo.setText(mLista.get(position).getTitle());
            lblabstract.setText(mLista.get(position).getAbstracts());
            lblFechaPublicacion.setText(mLista.get(position).getDate_published());

        }catch (Exception e){
            String res=e.toString();
        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }
}
