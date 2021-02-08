package com.example.hw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Button button_fetchAPI;
    private static final String api_url = "http://madlibz.herokuapp.com/api/random";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Look up button by its ID
        button_fetchAPI = findViewById(R.id.button_start);

        //add event listener for the click
        button_fetchAPI.setOnClickListener(new View.OnClickListener() { // 1st method - Implementation in Java
            @Override
            public void onClick(View v) {
                launchNextActivity(v);

            }
        });


    }

    public void launchNextActivity(View v) {
        client.addHeader("Accept", "*/*"); // HTTP Header for request
        client.get(api_url, new AsyncHttpResponseHandler() { // HTTP get request to retrieve info via API call
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("api response", new String(responseBody)); // Show what API call looks like

                try {
                    JSONObject json = new JSONObject(new String(responseBody)); // Convert string from API call into JSON
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class); // create intent - specify to and from
                    intent.putExtra("title", json.getString("title")); // Put title in intent for display in second activity
                    intent.putExtra("blanks", json.getString("blanks")); // Make JSON array of word types into string to put in intent
                    intent.putExtra("story", json.getString("value")); // Make JSON array of mad libs story into string pass into second activity, and then pass to third activity
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("api error", new String(responseBody)); // Just in case no api call or similar happens
            }
        });




    }


}