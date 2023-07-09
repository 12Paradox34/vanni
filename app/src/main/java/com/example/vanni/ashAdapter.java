package com.example.vanni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

public class ashAdapter extends ArrayAdapter<String> {
    private String[] items;


    public ashAdapter(@NonNull Context context, int resource, @NonNull String[] items) {
        super(context, resource, items);
        this.items=items;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return items[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,false);
        TextView t= convertView.findViewById(R.id.txtsongname);
        t.setText(getItem(position));
        return convertView;


    }


}
