package com.example.revistasuteq.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.revistasuteq.R;
import com.example.revistasuteq.objetos.Revista;

import java.util.ArrayList;

public class AdaptadorRevista extends RecyclerView.Adapter<AdaptadorRevista.MyViewHolder>
        implements View.OnClickListener  {
    private static final int TYPE_HEADER=0;
    private static final int TYPE_LIST=0;


    private Context mContext;

    private View.OnClickListener listener;
    private ArrayList<Revista> mLista;

    public TextView lblNombre;
    public ImageView imgPortada;

    public AdaptadorRevista(Context context, ArrayList<Revista> lista) {
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        int view_type;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblNombre=(TextView) itemView.findViewById(R.id.txtNombre);
            imgPortada=(ImageView) itemView.findViewById(R.id.imgPortada);
       }
    }

    @NonNull
    @Override
    public AdaptadorRevista.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revista,null,false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onBindViewHolder(@NonNull AdaptadorRevista.MyViewHolder holder, int position) {
        try {
            lblNombre.setText(mLista.get(position).getNombre());
            Glide.with(mContext)
                    .load(mLista.get(position).getPortada_url())
                    .into(imgPortada);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return mLista.size();
    }
}
