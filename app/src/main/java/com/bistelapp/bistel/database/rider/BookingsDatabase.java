package com.bistelapp.bistel.database.rider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.bistelapp.bistel.informations.rider.Bookings;

import java.util.ArrayList;

/**
 * Created by Control & Inst. LAB on 20-Sep-16.
 */
public class BookingsDatabase {

    BookingsHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public BookingsDatabase(Context context) {
        helper = new BookingsHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insertMyPost(ArrayList<Bookings> lists, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        String sql = "INSERT INTO " + BookingsHelper.TABLE_NAME_MYPOST + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
        //compile statement and start a transaction
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < lists.size(); i++) {
            Bookings current = lists.get(i);
            statement.clearBindings();

            statement.bindString(2, current.get_id);
            statement.bindString(3, current.driver_name);
            statement.bindString(4, current.driver_number);
            statement.bindString(5, current.driver_player_id);
            statement.bindString(6, current.plateNumber);
            statement.bindString(7, current.pickUp);
            statement.bindString(8, current.destination);
            statement.bindString(9, current.distance);
            statement.bindString(10, current.time);
            statement.bindString(11, current.amount);
            statement.bindString(12, current.payment_type);
            statement.bindString(13, current.booked_date);
            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<Bookings> getAllMyPosts() {
        ArrayList<Bookings> currentData = new ArrayList<>();

        String[] columns = {
                BookingsHelper.COLUMN_ID,
                BookingsHelper.COLUMN_GET_ID,
                BookingsHelper.COLUMN_DRIVER_NAME,
                BookingsHelper.COLUMN_DRIVER_NUMBER,
                BookingsHelper.COLUMN_DRIVER_PLAYER_ID,
                BookingsHelper.COLUMN_PLATE_NUMBER,
                BookingsHelper.COLUMN_PICKUP,
                BookingsHelper.COLUMN_DEST, BookingsHelper.COLUMN_DIST, BookingsHelper.COLUMN_TIME, BookingsHelper.COLUMN_AMOUNT,
                BookingsHelper.COLUMN_PAYMENT_TYPE, BookingsHelper.COLUMN_DATETIME
        };
        Cursor cursor = sqLiteDatabase.query(BookingsHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Bookings current = new Bookings();
                current.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_ID)));
                current.get_id = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_GET_ID));
                current.driver_name = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_DRIVER_NAME));
                current.driver_number = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_DRIVER_NUMBER));
                current.driver_player_id = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_DRIVER_PLAYER_ID));
                current.plateNumber = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_PLATE_NUMBER));
                current.pickUp = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_PICKUP));
                current.destination = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_DEST));
                current.distance = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_DIST));
                current.time = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_TIME));
                current.amount = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_AMOUNT));
                current.payment_type = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_PAYMENT_TYPE));
                current.booked_date = cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_DATETIME));
                currentData.add(current);
            }
            cursor.close();
        }

        return currentData;
    }

    public int getLastId() {
        int id = 0;
        String[] columns = {
                BookingsHelper.COLUMN_ID,
                BookingsHelper.COLUMN_GET_ID,
                BookingsHelper.COLUMN_DRIVER_NAME,
                BookingsHelper.COLUMN_DRIVER_NUMBER,
                BookingsHelper.COLUMN_DRIVER_PLAYER_ID,
                BookingsHelper.COLUMN_PLATE_NUMBER,
                BookingsHelper.COLUMN_PICKUP,
                BookingsHelper.COLUMN_DEST, BookingsHelper.COLUMN_DIST, BookingsHelper.COLUMN_TIME, BookingsHelper.COLUMN_AMOUNT,
                BookingsHelper.COLUMN_PAYMENT_TYPE, BookingsHelper.COLUMN_DATETIME
        };
        Cursor cursor = sqLiteDatabase.query(BookingsHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToLast();
            id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BookingsHelper.COLUMN_ID)));
            //cursor.close();
        }
        Log.e("gotID", "my id is " + id);
        return id;
    }

    public void deleteAll() {
        sqLiteDatabase.delete(BookingsHelper.TABLE_NAME_MYPOST, null, null);
    }

    public void updateDatabase(int foreignKey, String what_to_update, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(what_to_update, status);//BookingsHelper.COLUMN_STATUS
        sqLiteDatabase.update(BookingsHelper.TABLE_NAME_MYPOST, contentValues, BookingsHelper.COLUMN_ID + "=" + foreignKey, null);//
        Log.e("UPDATE", "database updated to " + status);
    }

    public void deleteDatabase(int id) {
        sqLiteDatabase.delete(BookingsHelper.TABLE_NAME_MYPOST, BookingsHelper.COLUMN_ID + "=" + id, null);
    }


    public class BookingsHelper extends SQLiteOpenHelper {

        private Context mContext;
        private static final String DB_NAME = "bookings_db";
        private static final int DB_VERSION = 3;

        public static final String TABLE_NAME_MYPOST = "bookings101";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_GET_ID = "get_id";
        public static final String COLUMN_DRIVER_NAME = "driver_name";
        public static final String COLUMN_DRIVER_NUMBER = "driver_number";
        public static final String COLUMN_DRIVER_PLAYER_ID = "driver_player_id";
        public static final String COLUMN_PLATE_NUMBER = "plateNumber";
        public static final String COLUMN_PICKUP = "pickUp";
        public static final String COLUMN_DEST = "destination";
        public static final String COLUMN_DIST = "distance";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_PAYMENT_TYPE = "payment_type";
        public static final String COLUMN_DATETIME = "booked_date";

        private static final String CREATE_TABLE_MYPOST = "CREATE TABLE " + TABLE_NAME_MYPOST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_GET_ID + " TEXT," +
                COLUMN_DRIVER_NAME + " TEXT," +
                COLUMN_DRIVER_NUMBER + " TEXT," +
                COLUMN_DRIVER_PLAYER_ID + " TEXT," +
                COLUMN_PLATE_NUMBER + " TEXT," +
                COLUMN_PICKUP + " TEXT," +
                COLUMN_DEST + " TEXT," +
                COLUMN_DIST + " TEXT," +
                COLUMN_TIME + " TEXT," +
                COLUMN_AMOUNT + " TEXT," +
                COLUMN_PAYMENT_TYPE + " TEXT," +
                COLUMN_DATETIME + " TEXT" +
                ");";


        public BookingsHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE_MYPOST);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(" DROP TABLE " + TABLE_NAME_MYPOST + " IF EXISTS;");
                onCreate(sqLiteDatabase);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
    }
}
