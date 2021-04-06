package com.example.rajitha.order.fragments;


/**
 * Created by admin on 7/7/18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rajitha.order.R;
import com.example.rajitha.order.database.DbHelper;
import com.example.rajitha.order.model.Item;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemAddFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int CAMERA_REQUEST = 1888;
    private  OnItemAddFragmentInteractionListener mListener ;
    String itemName, imageUrl;
    int itemCount;
    Uri imageURI;
    DbHelper dbHelper;
    Bitmap imagebit;
    ImageView image;
    EditText coupon;
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};

    public static ItemAddFragment newInstance() {
        ItemAddFragment fragment = new ItemAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        dbHelper = DbHelper.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_add, container, false);

        //Edit Text
        coupon = (EditText) view.findViewById(R.id.coupon);

        //Image View
        image = (ImageView) view.findViewById(R.id.image);

        //Button

        Button save = (Button) view.findViewById(R.id.save);


        // Spinner element
        Spinner spinner_item = (Spinner) view.findViewById(R.id.spinner_item);
        Spinner spinner_counter = (Spinner) view.findViewById(R.id.spinner_counter);

        // Spinner click listener
        spinner_item.setOnItemSelectedListener(this);
        spinner_counter.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> item = new ArrayList<String>();
        item.add("Item 1");
        item.add("Item 2");
        item.add("Item 3");
        item.add("Item 4");
        item.add("Item 5");
        item.add("Item 6");


        // Spinner Drop down elements
        List<Integer> counter = new ArrayList<Integer>();
        counter.add(1);
        counter.add(2);
        counter.add(3);
        counter.add(4);
        counter.add(5);
        counter.add(6);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterItem = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, item);
        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapterCounter = new ArrayAdapter<Integer>(this.getActivity(), android.R.layout.simple_spinner_item, counter);

        // Drop down layout style - list view with radio button
        dataAdapterItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Drop down layout style - list view with radio button
        dataAdapterCounter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_item.setAdapter(dataAdapterItem);
        spinner_counter.setAdapter(dataAdapterCounter);


        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item=new Item();
                item.setItemName(itemName);
                item.setCoupon(coupon.getText().toString());
                item.setImageUrl(imageUrl);
                item.setItemCount(itemCount);
                dbHelper.insertItem(item);
                mListener.onItemAdd();

            }
        });


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

        Spinner spinner_item = (Spinner) parent;
        Spinner spinner_counter = (Spinner) parent;
// On selecting a spinner item
        if (spinner_item.getId() == R.id.spinner_item) {
            itemName = parent.getItemAtPosition(i).toString();

        }
        if (spinner_counter.getId() == R.id.spinner_counter) {
            itemCount = Integer.parseInt(parent.getItemAtPosition(i).toString());

        }
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {

        if (resCode == Activity.RESULT_OK) {
            if (reqCode == 1) {
                imageURI = data.getData();
                imageUrl = imageURI.toString();
                image.setImageURI(data.getData());

            }

        }
        if (reqCode == CAMERA_REQUEST && resCode == Activity.RESULT_OK) {
            imagebit = (Bitmap) data.getExtras().get("data");
            imageUrl = convertBitmapToString(imagebit);
            image.setImageBitmap(imagebit);

        }

    }

    public static String convertBitmapToString(Bitmap src) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
//called once the fragment is associated with its activity.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemViewFragment.OnListFragmentInteractionListener) {
            mListener = (OnItemAddFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnItemAddFragmentInteractionListener {
        // TODO: Update argument type and name
        void onItemAdd();
    }
}
