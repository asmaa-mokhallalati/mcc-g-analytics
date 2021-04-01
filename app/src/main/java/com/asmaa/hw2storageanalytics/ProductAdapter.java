package com.asmaa.hw2storageanalytics;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asmaa.hw2storageanalytics.Model.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Item> items;

    public ProductAdapter(Activity activity, ArrayList<Item> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View v= LayoutInflater.from(activity).inflate(R.layout.product_layout,null);
        final TextView productName = v.findViewById(R.id.productNameTV);
        productName.setText(items.get(i).getName());
        productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("item", items.get(i));
                productName.getContext().startActivity(intent);
            }
        });
        return v;
    }
}
