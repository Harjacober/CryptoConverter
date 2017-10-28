package com.example.original_tech.cryptoconverter;

/**
 * Created by Original-Tech on 10/24/2017.
 */

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.original_tech.cryptoconverter.Utilities.NetworkUtils;
import com.example.original_tech.cryptoconverter.data.CurrencyContract;
import com.example.original_tech.cryptoconverter.data.CurrencyContract.CurrencyEntry;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements CryptoAdapter.ListItemClickListener{

    private List<JSONObject> cards;
    private CryptoAdapter cryptoAdapter;
    private String cryptoCurrencySelected;
    private String currencySelected;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mLoadIndicator;
    private RecyclerView mRecyclerView;
    private String[] mcryptoCurrency;
    private String[] mcurrency;
    private String processeCurrency;
    private String processedLastMarket="";
    private String processedLastUpdate="";
    private String processedPriceRate="";
    private String processedVolume="";
    private Spinner spinner;
    private Spinner spinner2;
    private int[] drawable;
    private int[] drawable2;
    private ImageView crycurrencyLogo;
    private ImageView currencyLogo;
    private ArrayList<Integer> exchangeId;
    List<JSONObject> list;
    CardView cardView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mcryptoCurrency = getResources().getStringArray(R.array.crypto_currency_array);
        mcurrency = getResources().getStringArray(R.array.currency_array);

        saveInDataBase();

        spinner = ((Spinner)findViewById(R.id.spinner));
        spinner2 = ((Spinner)findViewById(R.id.spinner2));
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.crypto_currency_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter1);
        setUpSpinnerListener();
        setUpDrawable();

        cards=new ArrayList<>();
        mLoadIndicator = ((ProgressBar)findViewById(R.id.cc_loading_indicator));
        mRecyclerView = ((RecyclerView)findViewById(R.id.recycler_view));
        cryptoAdapter = new CryptoAdapter(cards, this, this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(cryptoAdapter);

        crycurrencyLogo=(ImageView) findViewById(R.id.crypto_currency_logo);
        currencyLogo=(ImageView) findViewById(R.id.currency_logo);
        cardView=(CardView) findViewById(R.id.card_view);
        list=new ArrayList<>();
        exchangeId=new ArrayList<>();
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button create=(Button) findViewById(R.id.create_clicked);create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                prepareData();
                JSONObject json=new JSONObject();
                try {
                    json.put("cryptocurrency", cryptoCurrencySelected);
                    json.put("currency", currencySelected);
                }catch (Exception e){
                }
                list.add(json);
                cardView.setVisibility(View.GONE);
            }
        });
        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.add_card);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.VISIBLE);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            for (int i=0; i< list.size(); i++){
                try {
                    String cry = list.get(i).getString("cryptocurrency");
                    String cur = list.get(i).getString("currency");
                    new fetchCurrencyData().execute(cry,cur);
                }catch (Exception e){
                }
            }
        }
        else if (id==R.id.reload){
            for (int i=0; i< list.size(); i++){
                try {
                    String cry = list.get(i).getString("cryptocurrency");
                    String cur = list.get(i).getString("currency");
                    new fetchCurrencyData().execute(cry,cur);
                }catch (Exception e){
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareData() {

        new fetchCurrencyData().execute(cryptoCurrencySelected,currencySelected);
    }

    public void setUpSpinnerListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                cryptoCurrencySelected= (String) adapterView.getItemAtPosition(position);
                crycurrencyLogo.setImageResource(drawable2[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
      spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
              currencySelected= (String) adapterView.getItemAtPosition(position);
              currencyLogo.setImageResource(drawable[position]);
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });
    }

    public class fetchCurrencyData extends AsyncTask<String, Void, String> {
        String cryptocurrency,currency;
        public void onPreExecute() {
            super.onPreExecute();
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        public String doInBackground(String... strings) {
            cryptocurrency=strings[0];currency=strings[1];
            if(strings.length==0){
                return null;
            }
            String cryptoCurrency=strings[0];
            String currency=strings[1];
            URL localURL = NetworkUtils.buildUrl(cryptoCurrency, currency);
            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(localURL);
                return jsonString;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(String string) {
            mLoadIndicator.setVisibility(View.INVISIBLE);
            if (string != null) {
                try {
                    parseJSONData(string);
                    updateDataBase(cryptocurrency,currency);
                    getData();
                }
                catch (JSONException localJSONException) {
                    localJSONException.printStackTrace();
                    return;
                }
            }
            else{
                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void parseJSONData(String paramString) throws JSONException {

        JSONObject rootJsonObject = new JSONObject(paramString);
        JSONObject jsonObject= rootJsonObject.getJSONObject("DISPLAY");
        JSONObject cryObject = jsonObject.getJSONObject(cryptoCurrencySelected);
        JSONObject curObject = cryObject .getJSONObject(currencySelected);
        String price = curObject.getString("PRICE");
        String lastupdate = curObject.getString("LASTUPDATE");
        String volume24HOUR = curObject.getString("VOLUME24HOUR");
        String lastmarket = curObject.getString("LASTMARKET");
        String changepct24HOUR = curObject.getString("CHANGEPCT24HOUR");
        processedPriceRate = (price + "(" + changepct24HOUR + ")");
        processeCurrency = (cryptoCurrencySelected + " - " +currencySelected);
        processedVolume = (volume24HOUR);
        processedLastMarket = lastmarket;
        processedLastUpdate = lastupdate;
    }
    public void createContentValues(String... params) {

        ContentValues values = new ContentValues();
        values.put(CurrencyEntry.CRY_CURRENCY, params[0]);
        values.put(CurrencyEntry.CURRENCY, params[1]);
        values.put(CurrencyEntry.PRICE_RATE, params[2]);
        values.put(CurrencyEntry.VOLUME, params[3]);
        values.put(CurrencyEntry.LAST_MARKET, params[4]);
        values.put(CurrencyEntry.LAST_UPDATE, params[5]);
        getContentResolver().insert(CurrencyEntry.CONTENT_URI, values);
    }
    public void saveInDataBase() {
        for (int i = 0; i < mcryptoCurrency.length; i++) {
            for (int j = 0; j < mcurrency.length; j++) {
                String[] arrayOfString = new String[6];
                arrayOfString[0] = mcryptoCurrency[i];
                arrayOfString[1] = mcurrency[j];
                arrayOfString[2] = processedPriceRate;
                arrayOfString[3] = processedVolume;
                arrayOfString[4] = processedLastMarket;
                arrayOfString[5] = processedLastUpdate;
                createContentValues(arrayOfString);
            }
        }
    }
    public void updateDataBase(String cryptocurrency, String currency) {
        int i = getId(cryptocurrency, currency);
        exchangeId.add(i);
        Uri contentUri = ContentUris.withAppendedId(CurrencyContract.CurrencyEntry.CONTENT_URI, i);
        ContentValues values = new ContentValues();
        values.put("price_rate",processedPriceRate);
        values.put("volume", processedVolume);
        values.put("last_market", processedLastMarket);
        values.put("last_update", processedLastUpdate);
        getContentResolver().update(contentUri, values,
                null,
                null);
    }
    public int getId(String cryptocurrency, String currency) {
        if (cryptocurrency .equals("BTC")) {
            for (int k = 0; k < mcurrency.length; k++) {
                if (mcurrency[k].equals(currency)) {
                    return k + 1;
                }
            }
        }
        else {
            for (int j = 0; j < mcurrency.length; j++) {
                if (mcurrency[j].equals(currency)) {
                    return 20 + j + 1;
                }
            }
        }
            return 1;
    }
    public class fetchFromDataBase extends AsyncTask<String[],Void,Cursor>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(String[]... strings) {
            String[] projection=strings[0];
            Cursor cursor = getContentResolver().query
                    (CurrencyEntry.CONTENT_URI,
                            projection, null, null,
                            null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            mLoadIndicator.setVisibility(View.INVISIBLE);
            cursor.moveToFirst();
            cards.clear();
            while (!cursor.isAfterLast()) {
                if (!cursor.getString(cursor.getColumnIndex(CurrencyEntry.PRICE_RATE)).equals("")) {
                    JSONObject pob = new JSONObject();
                    try {
                        pob.put("cryptocurrency", cursor.getString(cursor.getColumnIndex(CurrencyEntry.CRY_CURRENCY)));
                        pob.put("currency", cursor.getString(cursor.getColumnIndex(CurrencyEntry.CURRENCY)));
                        pob.put("price_rate", cursor.getString(cursor.getColumnIndex(CurrencyEntry.PRICE_RATE)));
                        pob.put("volume", cursor.getString(cursor.getColumnIndex(CurrencyEntry.VOLUME)));
                        pob.put("last_market", cursor.getString(cursor.getColumnIndex(CurrencyEntry.LAST_MARKET)));
                        pob.put("last_update", cursor.getString(cursor.getColumnIndex(CurrencyEntry.LAST_UPDATE)));
                    }catch (JSONException e){
                    }
                    cards.add(pob);
                    cryptoAdapter.updateAdapter(cards);
                    Log.i("aaaaaaaaa",cursor.getString(cursor.getColumnIndex(CurrencyEntry.LAST_MARKET)));

                }
                cursor.moveToNext();
            }

        }
    }
    public void getData() throws JSONException {
        String[] projection = {CurrencyEntry.CRY_CURRENCY,
                CurrencyEntry.CURRENCY,
                CurrencyEntry.VOLUME,
                CurrencyEntry.PRICE_RATE,
                CurrencyEntry.LAST_UPDATE,
                CurrencyEntry.LAST_MARKET};
        new fetchFromDataBase().execute(projection);
    }
    @Override
    public void onListItemClick(int clickedItemIndex, String price, String currency) {
        String crySymbol=""+currency.charAt(0)+currency.charAt(1)+currency.charAt(2);
        String crySymbol2=""+currency.charAt(6)+currency.charAt(7)+currency.charAt(8);
        Log.i("kkkkk",String.valueOf(returnExchange(price)));
        String price1="1";
        Intent intent=new Intent(this,CurrencyConverter.class);
        intent.putExtra("crySymbol1",crySymbol);
        intent.putExtra("crySymbol2",crySymbol2);
        intent.putExtra("price2",returnExchange(price));
        intent.putExtra("price1",price1);
        startActivity(intent);
    }
    public void setUpDrawable(){
        drawable=new int[] {R.drawable.ngn,
                R.drawable.usd,
                R.drawable.cad,
                R.drawable.jpy,
                R.drawable.cny,
                R.drawable.krw,
                R.drawable.eur,
                R.drawable.chf,
                R.drawable.aud,
                R.drawable.hkd,
                R.drawable.gpb,
                R.drawable.jmd,
                R.drawable.jod,
                R.drawable.ils,
                R.drawable.azn,
                R.drawable.bsd,
                R.drawable.bhd,
                R.drawable.zmw,
                R.drawable.yer,
                R.drawable.vnd};
        drawable2=new int[] {R.drawable.bitcoin,R.drawable.eth};
    }
    Character func(char character){
        if (character=='1'||character=='2'||character=='3'||character=='4'||character=='5'||
                character==6||character==7||character=='8'||character=='9'||character=='.'||character=='0'){
            return character;
        }
        return null;
    }
    String returnExchange(String price){
        int i;
        String exchange="";
        for (i=0; i<price.length(); i++){
            if (price.charAt(i)=='(') {
                i=100;
            }
            else {
                Character a = func(price.charAt(i));
                if (a !=  null) {
                    exchange += a;
                }
            }
        }
        return exchange;
    }
    public void updateAllData() {
        /*for (int i=0; i < exchangeId.size(); i++) {
            Uri currencyUri = ContentUris.withAppendedId(CurrencyEntry.CONTENT_URI, exchangeId.get(i));
            String[] projection = {CurrencyEntry.CRY_CURRENCY,
                    CurrencyEntry.CURRENCY};
            Cursor cursor = getContentResolver().query
                    (currencyUri,
                            projection, null, null,
                            null);
            cursor.moveToPosition(exchangeId.get(i));
            updateDataBase("BTC", "USD");
            cursor.close();
        }*/
        updateDataBase("BTC", "USD");
    }
}
