package pl.poznan.put.cs.io.beerdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private String User_Agent = "beerdiary";
    private JsonReader jsonReader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void beerButtonOnClick(View v) {

    }

    public void breweryButtonOnClick(View v) {
        Intent intent = new Intent(MainActivity.this, BreweryScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void pubButtonOnClick(View v) {
        Intent intent = new Intent(MainActivity.this, PubScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /*
    public void pubButtonOnClick(View v) {

        // Create a new thread to connect to the given URL
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Create URL
                URL targetURL = null;
                try {
                    targetURL = new URL("http://164.132.101.153:8000/api/pubs/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Create connection
                HttpURLConnection myConnection = null;
                try {
                    if (targetURL == null)
                        throw new IOException();
                    myConnection = (HttpURLConnection) targetURL.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if(myConnection == null)
                        throw new IOException();
                    myConnection.setRequestProperty("User-Agent", "beerdiary");
                    myConnection.setRequestProperty("Token", "f2e4442fd010cb15f53e32277a09f480ec7b58c2");

                    if(myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");


                        jsonReader = new JsonReader(responseBodyReader);

                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            jsonReader.beginObject(); // Start processing the JSON object
                            while (jsonReader.hasNext()) { // Loop through all keys
                                String key = jsonReader.nextName(); // Fetch the next key
                                if (key.equals("name")) { // Check if desired key
                                    // Fetch the value as a String
                                    final String value = jsonReader.nextString();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TextView testLabel = (TextView) findViewById(R.id.TestLabel);
                                            testLabel.setText(value);
                                        }
                                    });
                                } else {
                                    jsonReader.skipValue(); // Skip values of other keys
                                }
                            }
                            jsonReader.endObject();
                        }
                        jsonReader.endArray();


                    }
                    else {

                        final String ResponseCode = String.valueOf(myConnection.getResponseCode());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView testLabel = (TextView) findViewById(R.id.TestLabel);
                                testLabel.setText(ResponseCode);
                            }
                        });

                        jsonReader = null;
                    }

                    myConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
