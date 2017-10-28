package com.example.original_tech.cryptoconverter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class CurrencyConverter extends AppCompatActivity {

    private String cryptoCurrency;
    private String currency;
    private String cryptoCurrencySymnol;
    private String currencySymbol;
    private TextView currencyText;
    private TextView cryptoCurrencyText;
    private EditText currencyValue;
    private TextView result;
    private EditText cryptoCurrencyValue;
    private boolean keppTextChenged=true;
    DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        cryptoCurrencyText=(TextView) findViewById(R.id.crypto_currency_text);
        currencyText=(TextView) findViewById(R.id.currency_text);
        cryptoCurrencyValue=(EditText) findViewById(R.id.crypto_currency_value);
        currencyValue=(EditText) findViewById(R.id.currency_value);
        result=(TextView) findViewById(R.id.result);
        ImageView cryptoCurrencyLogo = (ImageView) findViewById(R.id.crypto_currency_logo);
        ImageView currencyLogo = (ImageView) findViewById(R.id.currency_logo);

        getIntentPassed();
        setIntentsPassed();
        processConversion();
        setTitle(cryptoCurrencySymnol+" - "+currencySymbol+" Converter");
        decimalFormat=new DecimalFormat(".###");
    }

    public void getIntentPassed(){
        Intent intent=getIntent();
        cryptoCurrency=intent.getStringExtra("price1");
        currency=intent.getStringExtra("price2");
        cryptoCurrencySymnol=intent.getStringExtra("crySymbol1");
        currencySymbol=intent.getStringExtra("crySymbol2");
    }
    public void setIntentsPassed(){
        cryptoCurrencyText.setText(cryptoCurrencySymnol);
        currencyText.setText(currencySymbol);
        cryptoCurrencyValue.setText(cryptoCurrency);
        currencyValue.setText(currency);

    }
    public void processConversion() {
        cryptoCurrencyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double cryValue1 = Double.valueOf(cryptoCurrency);
                double curValue1 = Double.valueOf(currency);
                    if (!cryptoCurrencyValue.getText().toString().trim().isEmpty()) {
                        double userInput1=1;
                        try {
                            userInput1 = Double.valueOf(cryptoCurrencyValue.getText().toString().trim());
                        }catch (Exception e){
                            Snackbar.make(cryptoCurrencyValue,"exception",Snackbar.LENGTH_SHORT);
                        }
                        String equivalent1=decimalFormat.format((userInput1 * curValue1) / cryValue1);
;                        if (keppTextChenged) {
                            keppTextChenged = false;
                            currencyValue.setText(equivalent1);
                            result.setText(cryptoCurrencyValue.getText().toString() + "  " +
                                    cryptoCurrencyText.getText() + "  is  " +
                                    currencyValue.getText() + "  " +
                                    currencyText.getText());
                        }
                        keppTextChenged = true;
                    } else {
                        if (keppTextChenged) {
                            keppTextChenged=false;
                            currencyValue.setText("");
                        }
                        keppTextChenged=true;
                    }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        currencyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double cryValue2 = Double.valueOf(cryptoCurrency);
                double curValue2 = Double.valueOf(currency);
                if (!currencyValue.getText().toString().trim().isEmpty()) {
                    double userInput2 = Double.valueOf(currencyValue.getText().toString().trim());
                    String equivalent2 = decimalFormat.format((userInput2 * cryValue2) / curValue2);
                    if (keppTextChenged){
                        keppTextChenged=false;
                        cryptoCurrencyValue.setText(equivalent2);
                        result.setText(cryptoCurrencyValue.getText().toString()+ " "+
                                cryptoCurrencyText.getText()+"  is  "+
                                currencyValue.getText()+" "+
                                currencyText.getText());
                    }
                    keppTextChenged=true;
                }
                else {
                    if (keppTextChenged) {
                        keppTextChenged=false;
                        cryptoCurrencyValue.setText("");
                    }
                    keppTextChenged=true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
