package com.devbyrod.distancetocity;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Created by Rodrigo on 10/31/2014.
 */
public class HttpCallable implements Callable< String > {

    private String searchCriteria;

    public HttpCallable( String searchCriteria ){

        this.searchCriteria = searchCriteria;
    }

    @Override
    public String call() throws Exception {

        String url = Constants.FOURSQUARE_API_URL +
                    Constants.SEARCH_CRITERIA_NEAR + "=" + this.searchCriteria + "&" +
                    "client_id=" + Constants.FOURSQUARE_API_CLIENT_ID + "&" +
                    "client_secret=" + Constants.FOURSQUARE_API_CLIENT_SECRET + "&" +
                    "v=" + Constants.FOURSQUARE_API_VERSION;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost( url );

        try {

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpPost);
            Log.d("HTTP Response", response.getStatusLine().toString());

            InputStream inputStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = null;

            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");
            }

            Log.d("HttpRequestThread:", "Success: " + sb.toString());
            return sb.toString();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}
