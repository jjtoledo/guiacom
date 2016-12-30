package com.jose.guiacom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import static com.jose.guiacom.Urls.img;

/**
 * Created by Jose on 27/12/2016.
 */

public class CustomSwipeAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Foto> fotos;
    private int tipo;
    private Integer position;

    public CustomSwipeAdapter(Context context, ArrayList<Foto> fotos, int tipo, Integer position){
        this.context = context;
        this.fotos = new ArrayList<>();
        this.fotos = fotos;
        this.tipo = tipo;
        this.position = position;
    }

    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v;

        if (tipo == 1) {
            v = inflater.inflate(R.layout.swipe, container, false);
            ImageView iv = (ImageView) v.findViewById(R.id.foto);
            Picasso.with(context)
                    .load(img + fotos.get(position).foto)
                    .into(iv);
            container.addView(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FotoActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("fotos", fotos);
                    context.startActivity(intent);
                }
            });
        } else {
            v = inflater.inflate(R.layout.swipe_fotos, container, false);
            ImageView iv = (ImageView) v.findViewById(R.id.foto);
            Picasso.with(context)
                    .load(img + fotos.get(position).foto)
                    .into(iv);
            TextView descricao = (TextView) v.findViewById(R.id.descricao);
            descricao.setText(fotos.get(position).descricao);
            container.addView(v);
        }

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}
