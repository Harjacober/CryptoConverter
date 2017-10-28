package com.example.original_tech.cryptoconverter.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Original-Tech on 10/21/2017.
 */
public final class NetworkUtils {
    private static final String BASE_URL="https://min-api.cryptocompare.com/data/pricemultifull?";
    private static final String CRYPTOCURRENCY_PARAMS="fsyms=";
    private static final String CURRENCY_PARAMS="tsyms=";
    private static final String CoNSTANT="&";
    public static URL buildUrl(String cryptocurrency,String currency){
        Uri builtUri=Uri.parse(BASE_URL+CRYPTOCURRENCY_PARAMS+cryptocurrency+CoNSTANT+CURRENCY_PARAMS+currency).
                buildUpon().build();
        URL url=null;
        try{
            url=new URL(builtUri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        try{
            httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            inputStream=httpURLConnection.getInputStream();
            Scanner scanner=new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput=scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }
            else{
                return null;
            }
        }
        finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
    }
}
