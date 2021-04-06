package com.example.rajitha.order.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajitha.order.R;
import com.example.rajitha.order.fragments.ItemViewFragment.OnListFragmentInteractionListener;
import com.example.rajitha.order.model.Item;

import java.util.List;

/*
A bridge between an AdapterView and the underlying data for that view
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<Item> mValues;
    private final OnListFragmentInteractionListener mListener;

//Constructor
    public ItemRecyclerViewAdapter(List<Item> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.item_name.setText(mValues.get(position).getItemName());

        Bitmap image = StringToBitMap(mValues.get(position).getImageUrl());
        holder.item_image.setImageBitmap(image);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete", "test delete");
                mListener.onListFragmentInteraction(mValues.get(position));


            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    // convert String to Bitmap
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView item_name;
        public final ImageView item_image;
        public final ImageView delete;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_image = (ImageView) view.findViewById(R.id.item_image);
            delete = (ImageView) view.findViewById(R.id.delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + item_name.getText() + "'";
        }
    }

}
