package com.wristcode.deliwala.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ajay Jagadish on 07-Mar-18.
 */

public class ExampleDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Deliwala.db";
    private static final int DATABASE_VERSION = 1;
    public static final String SUBCAT_TABLE_NAME = "item";
    public static final String SUBCAT_COLUMN_ID = "item_id";
    public static final String SUBCAT_COLUMN_RESID = "res_id";
    public static final String SUBCAT_COLUMN_NAME = "item_name";
    public static final String SUBCAT_COLUMN_QUANTITY = "item_qty";
    public static final String SUBCAT_COLUMN_PRICE = "item_price";
    public static final String SUBCAT_COLUMN_ACTUALPRICE = "item_actualprice";
    public static final String SUBCAT_COLUMN_IMAGE = "item_image";

    public ExampleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SUBCAT_TABLE_NAME + "(" +
                SUBCAT_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                SUBCAT_COLUMN_RESID + " INTEGER, " +
                SUBCAT_COLUMN_NAME + " TEXT, " +
                SUBCAT_COLUMN_QUANTITY + " INTEGER, " +
                SUBCAT_COLUMN_PRICE + " INTEGER, " +
                SUBCAT_COLUMN_ACTUALPRICE + " INTEGER, " +
                SUBCAT_COLUMN_IMAGE + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SUBCAT_TABLE_NAME);
        onCreate(db);
    }

    public String insertItem(int item_id, int res_id, String item_name, int item_qty, int item_price, int item_actualprice, String item_image) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Str_Return_Value = null;
        long query_ret;
        try {
            ContentValues values = new ContentValues();
            values.put(SUBCAT_COLUMN_ID, item_id);
            values.put(SUBCAT_COLUMN_RESID, res_id);
            values.put(SUBCAT_COLUMN_NAME, item_name);
            values.put(SUBCAT_COLUMN_QUANTITY, item_qty);
            values.put(SUBCAT_COLUMN_PRICE, item_price);
            values.put(SUBCAT_COLUMN_ACTUALPRICE, item_actualprice);
            values.put(SUBCAT_COLUMN_IMAGE, item_image);
            query_ret = db.insertWithOnConflict(SUBCAT_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if (query_ret != -1) {
                Str_Return_Value = "success";
            } else {
                Str_Return_Value = "failure";
            }
        } catch (Exception e) {
        }
        db.close();
        return Str_Return_Value;
    }

    public boolean updateItem(int item_id, int res_id, String item_name, int item_qty, int item_price, int item_actualprice, String item_image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBCAT_COLUMN_ID, item_id);
        contentValues.put(SUBCAT_COLUMN_RESID, res_id);
        contentValues.put(SUBCAT_COLUMN_NAME, item_name);
        contentValues.put(SUBCAT_COLUMN_QUANTITY, item_qty);
        contentValues.put(SUBCAT_COLUMN_PRICE, item_price);
        contentValues.put(SUBCAT_COLUMN_ACTUALPRICE, item_actualprice);
        contentValues.put(SUBCAT_COLUMN_IMAGE, item_image);
        db.update(SUBCAT_TABLE_NAME, contentValues, SUBCAT_COLUMN_ID + "=?", new String[]{Integer.toString(item_id)});
        return true;
    }

    public Cursor getItem(int item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + SUBCAT_TABLE_NAME + " WHERE " + SUBCAT_COLUMN_ID + "=" + item_id;
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + SUBCAT_TABLE_NAME;
        return db.rawQuery(selectQuery, null);
    }

    public Integer deleteItem(int item_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SUBCAT_TABLE_NAME,
                SUBCAT_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(item_id)});
    }

    public int gettotalqty() {
        int qtyValue = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        final Cursor cursor1 = database.rawQuery("SELECT SUM(item_qty) FROM " + SUBCAT_TABLE_NAME, null);
        if (cursor1 != null) {
            try {
                if (cursor1.moveToFirst()) {
                    qtyValue = cursor1.getInt(0);
                }
            } finally {
                cursor1.close();
            }
        }
        return qtyValue;
    }

    public int gettotalprice() {
        int priceValue = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        final Cursor cursor1 = database.rawQuery("SELECT SUM(item_price) FROM " + SUBCAT_TABLE_NAME, null);
        if (cursor1 != null) {
            try {
                if (cursor1.moveToFirst()) {
                    priceValue = cursor1.getInt(0);
                }
            } finally {
                cursor1.close();
            }
        }
        return priceValue;
    }

    public int getQuantity(int item_id) {
        int qty = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String strSql = "SELECT * FROM " + SUBCAT_TABLE_NAME + " WHERE " + SUBCAT_COLUMN_ID + " = " + item_id;
        Cursor cursor = db.rawQuery(strSql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            qty = cursor.getInt(cursor.getColumnIndex(SUBCAT_COLUMN_QUANTITY));
        }
        return qty;
    }

    public boolean checksubid(int item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM " + SUBCAT_TABLE_NAME + " WHERE " + SUBCAT_COLUMN_ID + "=" + item_id, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, SUBCAT_TABLE_NAME);
        db.close();
        return cnt;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Delete from " + SUBCAT_TABLE_NAME);
    }
}
