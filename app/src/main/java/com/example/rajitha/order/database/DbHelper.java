package com.example.rajitha.order.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rajitha.order.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajitha on 2/17/2017.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";
    // Database Info
    private static final String DATABASE_NAME = "ItemDatabase";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_ITEM = "Item";


    //itemetail Table Columns

    private static final String ID = "id";
    private static final String ITEM_NAME = "itemName";
    private static final String ITEM_COUNT = "itemCount";
    private static final String COUPON = "coupon";
    private static final String IMAGE = "imageUrl";

    private static DbHelper mDbHelper;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ITEM = "CREATE TABLE " + TABLE_ITEM +
                "(" +
                ID + " integer primary key autoincrement," +
                ITEM_NAME + " TEXT," +
                ITEM_COUNT + " integer," +
                COUPON + " TEXT," +
                IMAGE + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE_ITEM);
        Log.d(TAG, "Created");
    }

  /*Called when the database needs to be upgraded.
   The implementation should use this method to drop tables, add tables,
   or do anything else it needs to upgrade to the new schema version.
   */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
            onCreate(db);
        }
    }


    public static synchronized DbHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (mDbHelper == null) {
            mDbHelper = new DbHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //insert Item

    public void insertItem(Item item) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();


        try {
            ContentValues values = new ContentValues();
            values.put(ITEM_NAME, item.getItemName());
            values.put(ITEM_COUNT, item.getItemCount());
            values.put(COUPON, item.getCoupon());
            values.put(IMAGE, item.getImageUrl());

            db.insertOrThrow(TABLE_ITEM, null, values);
            db.setTransactionSuccessful();
            Log.d(TAG, "Data Saved");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to add post to database");
        } finally {

            db.endTransaction();
        }


    }

    //Get all Item list
    public List<Item> getAllItems() {

        List<Item> itemList = new ArrayList<>();

        final String ITEM_DETAIL_SELECT_QUERY = "SELECT * FROM " + TABLE_ITEM;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEM_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    Item item = new Item();
                    item.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                    item.setItemCount(cursor.getInt(cursor.getColumnIndex(ITEM_COUNT)));
                    item.setCoupon(cursor.getString(cursor.getColumnIndex(COUPON)));
                    item.setImageUrl(cursor.getString(cursor.getColumnIndex(IMAGE)));

                    itemList.add(item);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return itemList;

    }

    //Delete Item
    public void deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.beginTransaction();
            db.execSQL("delete from " + TABLE_ITEM + " WHERE " + ID + "='" + id + "'");
            db.setTransactionSuccessful();
            Log.d(TAG, "Done delete");
        } catch (SQLException e) {
            Log.d(TAG, "Error while trying to delete  users detail");
        } finally {
            db.endTransaction();
        }


    }

}