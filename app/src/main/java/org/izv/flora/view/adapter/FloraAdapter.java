package org.izv.flora.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.izv.flora.R;
import org.izv.flora.model.entity.Flora;
import org.izv.flora.view.FloraDetailActivity;
import org.izv.flora.view.adapter.viewholder.FloraViewHolder;
import org.izv.flora.viewmodel.MainActivityViewModel;

import java.util.ArrayList;

public class FloraAdapter extends RecyclerView.Adapter<FloraViewHolder> {

    Context context;
    ArrayList<Flora> lista;

    public FloraAdapter(Context ctx){
        this.context = ctx;
    }

    public void setLista(ArrayList<Flora> lista) {
        this.lista = lista;
        notifyDataSetChanged(); 
    }

    @NonNull
    @Override
    public FloraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.floraitem, parent, false);
        return new FloraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FloraViewHolder holder, int position) {
        Flora flora = lista.get(position);

        String url = "https://informatica.ieszaidinvergeles.org:10004/ad/felixRDLFApp/public/api/imagen/" + flora.getId() + "/flora";
        Glide.with(context).load(url).into(holder.ivFlora);
        holder.tvName.setText(flora.getNombre());
        holder.tvFamily.setText(flora.getFamilia());

        holder.itemView.setOnClickListener((View v) -> {
            Intent intent = new Intent(context, FloraDetailActivity.class);
            intent.putExtra("flora", flora);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (lista != null)
            return  lista.size();
        return 0;
    }
}
