package com.londonappbrewery.bitcointicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by RamsesPC on 11/16/2017.
 */

public class BitcoinDataClient
{
    private static final String BASE_URL = "https://apiv2.bitcoinaverage.com/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getBitcoinData(String currencyType, AsyncHttpResponseHandler responseHandler)
    {
        client.get(getUrlForTicker("global", currencyType), responseHandler);
    }

    private static String getUrlForTicker(String market, String currencyType)
    {
        return BASE_URL + "indices/" + market + "/ticker/" + "BTC" + currencyType;
    }
}
