package com.example.original_tech.cryptoconverter.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.original_tech.cryptoconverter.R;

/**
 * Created by Original-Tech on 10/23/2017.
 */
public class CurrencyProvider extends ContentProvider {
    CurrencyDbHelper currencyDbHelper;
    private static final int CURRENCY=100;
    private static final int CURRENCY_WITH_ID=101;
    private static final UriMatcher uriMatcher=buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher suriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        suriMatcher.addURI(CurrencyContract.CONTENT_AUTHORITY, CurrencyContract.CurrencyEntry.TABLE_NAME,CURRENCY);
        suriMatcher.addURI(CurrencyContract.CONTENT_AUTHORITY, CurrencyContract.CurrencyEntry.TABLE_NAME+"/#",CURRENCY_WITH_ID);
        return suriMatcher;
    }

    @Override
    public boolean onCreate() {
        currencyDbHelper=new CurrencyDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortorder) {
        SQLiteDatabase db=currencyDbHelper.getReadableDatabase();
        int match=uriMatcher.match(uri);
        Cursor cursor;
        switch (match){
            case CURRENCY:
                cursor=db.query(CurrencyContract.CurrencyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortorder);
                break;
            case CURRENCY_WITH_ID:
                selection= CurrencyContract.CurrencyEntry._ID+"=?";
                selectionArgs=new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(CurrencyContract.CurrencyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortorder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = currencyDbHelper.getWritableDatabase();
        Uri returnUri;
        int match = uriMatcher.match(uri);
        switch (match) {
            case CURRENCY: {
                long id = db.insert(CurrencyContract.CurrencyEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = CurrencyContract.CurrencyEntry.buildReminderUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into  " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri " + uri);
            }
        }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase=currencyDbHelper.getWritableDatabase();
        int numdeleted;
        int match=uriMatcher.match(uri);
        switch (match){
            case CURRENCY:
                numdeleted=sqLiteDatabase.delete(CurrencyContract.CurrencyEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                        + CurrencyContract.CurrencyEntry.TABLE_NAME+"'");
                break;
            case CURRENCY_WITH_ID:
                numdeleted=sqLiteDatabase.delete(CurrencyContract.CurrencyEntry.TABLE_NAME,
                        CurrencyContract.CurrencyEntry._ID+"=?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                        + CurrencyContract.CurrencyEntry.TABLE_NAME+"'");
                break;
            default:
                throw new UnsupportedOperationException("Unlnown uri "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return numdeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase=currencyDbHelper.getWritableDatabase();
        int numupdated=0;
        if (contentValues==null){
            throw new IllegalArgumentException("cannot have null content values");
        }
        int match=uriMatcher.match(uri);
        switch (match){
            case CURRENCY:
                numupdated=sqLiteDatabase.update(CurrencyContract.CurrencyEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case CURRENCY_WITH_ID:
                numupdated=sqLiteDatabase.update(CurrencyContract.CurrencyEntry.TABLE_NAME,
                        contentValues,
                        CurrencyContract.CurrencyEntry._ID+"=?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri "+uri);
        }
        if (numupdated>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numupdated;
    }
}
