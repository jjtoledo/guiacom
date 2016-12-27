package com.jose.guiacom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList items;
    private LayoutInflater vi;
    public EntryAdapter(Context context,ArrayList items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final Item i = (Item) items.get(position);
        if (i != null) {
            if(i.isSection()){
                SectionItem si = (SectionItem)i;
                v = vi.inflate(R.layout.header, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                final TextView sectionView =
                        (TextView) v.findViewById(R.id.tvNome);
                sectionView.setText(si.getTitle());
            } else {
                EntryItem ei = (EntryItem)i;
                v = vi.inflate(R.layout.layout_empresas_admin, null);
                final TextView nome =
                        (TextView)v.findViewById(R.id.tvNome);
                final TextView cidade =
                        (TextView)v.findViewById(R.id.tvCidade);
                final TextView telefone =
                        (TextView)v.findViewById(R.id.tvTel);
                if (nome != null) nome.setText(ei.nome);
                if (cidade != null) cidade.setText(ei.cidade);
                if (telefone != null) telefone.setText(ei.telefone);
            }
        }
        return v;
    }
}