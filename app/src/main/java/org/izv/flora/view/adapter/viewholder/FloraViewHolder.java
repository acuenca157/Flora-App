package org.izv.flora.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.izv.flora.R;

public class FloraViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivFlora;
    public TextView tvName, tvFamily;

    public FloraViewHolder(@NonNull View itemView) {
        super(itemView);
        ivFlora = itemView.findViewById(R.id.ivImagenFlora);
        tvName = itemView.findViewById(R.id.tvNombreFlora);
        tvFamily = itemView.findViewById(R.id.tvFamiliaFlora);
    }
}
