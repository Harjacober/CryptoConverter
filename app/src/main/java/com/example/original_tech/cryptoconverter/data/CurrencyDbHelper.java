package com.example.original_tech.cryptoconverter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Original-Tech on 10/23/2017.
 */
public class CurrencyDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="CryptoConverter.db";
    public static final int DATABASE_VERSION=1;
    public CurrencyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CURRENCY_TABLE="CREATE TABLE "+ CurrencyContract.CurrencyEntry.TABLE_NAME+"("
                + CurrencyContract.CurrencyEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CurrencyContract.CurrencyEntry.VOLUME+" TEXT NOT NULL, "
                + CurrencyContract.CurrencyEntry.CRY_CURRENCY+" TEXT NOT NULL, "
                + CurrencyContract.CurrencyEntry.CURRENCY+" TEXT NOT NULL, "
                + CurrencyContract.CurrencyEntry.PRICE_RATE+" TEXT NOT NULL, "
                + CurrencyContract.CurrencyEntry.LAST_MARKET+" TEXT NOT NULL, "
                + CurrencyContract.CurrencyEntry.LAST_UPDATE+" TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_CURRENCY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE="DROP IF EXIST "+ CurrencyContract.CurrencyEntry.TABLE_NAME+";";
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
