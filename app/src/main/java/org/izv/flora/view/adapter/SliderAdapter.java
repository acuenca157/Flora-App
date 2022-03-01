package org.izv.flora.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.izv.flora.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder>{

    int[] images;
    Context context;

    public SliderAdapter(Context context, int[] images){

        this.context = context;
        this.images = images;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

        String url = "https://informatica.ieszaidinvergeles.org:10004/ad/felixRDLFApp/public/api/imagen/" + images[position];
        Glide.with(context).load(url).into(viewHolder.imageView);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }

}
