package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity
{
    private final float LOADING_CURRENCY = -1337;
    // Member Variables:
    TextView mPriceTextView;
    String currencyType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);
        Button buttonView = (Button)findViewById(R.id.button_refresh);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyType = parent.getItemAtPosition(position).toString();
                updateUI(LOADING_CURRENCY, null);
                doBitcoinDataRequest(currencyType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currencyType == null) { return; }
                updateUI(LOADING_CURRENCY, null);
                doBitcoinDataRequest(currencyType);
            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void doBitcoinDataRequest(final String currencyType)
    {
        BitcoinDataClient.getBitcoinData(currencyType, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    float value = (float)response.getDouble("last");
                    updateUI(value, currencyType);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Bitcoin", "Failed: " + throwable.toString());
                Toast.makeText(MainActivity.this,"Loading Currency Failed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("Bitcoin", "Failed{2}: " + throwable.toString());
                Toast.makeText(MainActivity.this,"Loading Currency Failed{2}.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(float amount, String currencyType)
    {
        String textToDisplay = getString(R.string.no_currency);
        if(currencyType != null) {
            textToDisplay = amount +" "+ currencyType;
        }
        else if(amount == LOADING_CURRENCY){
            textToDisplay = getString(R.string.loading_currency);
        }
        Log.d("Bitcoin", amount  +" "+ LOADING_CURRENCY + " " + (amount  == LOADING_CURRENCY));
        mPriceTextView.setText(textToDisplay);
    }

}
