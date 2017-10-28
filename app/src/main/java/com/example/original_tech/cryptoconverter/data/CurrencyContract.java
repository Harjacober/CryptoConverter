package com.example.original_tech.cryptoconverter.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

/**
 * Created by Original-Tech on 10/23/2017.
 */
public class CurrencyContract {
    public static final String CONTENT_AUTHORITY="com.example.original_tech.cryptoconverter";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public CurrencyContract() {}

    public static class CurrencyEntry implements BaseColumns{
        public static final String _ID=BaseColumns._ID;
        public static final String TABLE_NAME="currency";
        public static final String VOLUME="volume";
        public static final String CRY_CURRENCY="cryptocurrency";
        public static final String CURRENCY="mcurrency";
        public static final String PRICE_RATE="price_rate";
        public static final String LAST_MARKET="last_market";
        public static final String LAST_UPDATE="last_update";
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static Uri buildReminderUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

}
