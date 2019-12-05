package com.vmb.hoingu.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.vmb.hoingu.config.Config;

/**
 * Created by keban on 3/6/2018.
 */

public class HoiNgu extends SQLiteAssetHelper {

    public HoiNgu(Context context, String database_name) {
        super(context, database_name, null, Config.SCHEMA_VERSION);
    }

    public Cursor getCursor() {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {Config.HOINGU_ID, Config.HOINGU_CAUHOI, Config.HOINGU_A, Config.HOINGU_B, Config.HOINGU_C, Config.HOINGU_D,
                Config.HOINGU_DAPAN, Config.HOINGU_NICK, Config.HOINGU_GIAITHICH};

        Cursor cursor = db.query(Config.HOINGU_TABLE, columns, null, null, null, null, null);
        return cursor;
    }

    public int getID(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Config.HOINGU_ID));
        return id;
    }

    public String getCauhoi(Cursor cursor) {
        String cauhoi = cursor.getString(cursor.getColumnIndex(Config.HOINGU_CAUHOI));
        return cauhoi;
    }

    public String getA(Cursor cursor) {
        String a = cursor.getString(cursor.getColumnIndex(Config.HOINGU_A));
        return a;
    }

    public String getB(Cursor cursor) {
        String b = cursor.getString(cursor.getColumnIndex(Config.HOINGU_B));
        return b;
    }

    public String getC(Cursor cursor) {
        String c = cursor.getString(cursor.getColumnIndex(Config.HOINGU_C));
        return c;
    }

    public String getD(Cursor cursor) {
        String d = cursor.getString(cursor.getColumnIndex(Config.HOINGU_D));
        return d;
    }

    public String getDapan(Cursor cursor) {
        String dapan = cursor.getString(cursor.getColumnIndex(Config.HOINGU_DAPAN));
        return dapan;
    }

    public String getNick(Cursor cursor) {
        String nick = cursor.getString(cursor.getColumnIndex(Config.HOINGU_NICK));
        return nick;
    }

    public String getGiaithich(Cursor cursor) {
        String giaithich = cursor.getString(cursor.getColumnIndex(Config.HOINGU_GIAITHICH));
        return giaithich;
    }
}
