package com.example.rajitha.order.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.rajitha.order.R;
import com.example.rajitha.order.database.DbHelper;
import com.example.rajitha.order.fragments.AboutUsFragment;
import com.example.rajitha.order.fragments.ItemAddFragment;
import com.example.rajitha.order.fragments.ItemViewFragment;
import com.example.rajitha.order.model.Item;

public class MainActivity extends AppCompatActivity implements ItemViewFragment.OnListFragmentInteractionListener, ItemAddFragment.OnItemAddFragmentInteractionListener {


    DbHelper dbHelper;

    //creates bottom navigation bars
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    selectedFragment = ItemAddFragment.newInstance();
                    break;
                case R.id.navigation_view:
                    selectedFragment = ItemViewFragment.newInstance();
                    break;
                case R.id.navigation_contact:
                    selectedFragment = AboutUsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            //Schedules a commit of this transaction
            transaction.commit();
            return true;

        }
    };

    //When activity start onCreate calls
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize sqlite database
        dbHelper = DbHelper.getInstance(this);

        //BottomNavigationView
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemAddFragment.newInstance());
        transaction.commit();

    }

    @Override
    public void onListFragmentInteraction(Item item) {
        dbHelper.deleteItem(item.getId());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemViewFragment.newInstance());
        transaction.commit();

    }

    @Override
    public void onItemAdd() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemAddFragment.newInstance());
        transaction.commit();
    }
}
