package pl.poznan.put.cs.io.beerdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ModifyPubScreen extends AppCompatActivity {

    private String User_Agent = "beerdiary";
    private JsonReader jsonReader = null;
    String pubsURL = "http://164.132.101.153:8000/api/pubs/";

    // robie dzis tylko dodawanie; docelowo ten ekran powinien wiedziec, czy dodajemy, czy edytujemy, i dodatkowo pamietac indeks edytowanego pubu.
    boolean addingPub = true;
    // TODO add PUT method and keep pub id in the class
    int pubId = -1;

    EditText NameText;
    EditText StreetText;
    EditText CityText;
    EditText DesignText;
    EditText AtmosphereText;
    RatingBar OverallRating;

    String Name;
    String Street;
    String City;
    String DesignDesc;
    String AtmosphereDesc;
    int OverallInt;
    Rating Overall;

    JSONObject pubJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Puby");
        setContentView(R.layout.modify_pub);

        OverallRating  = (RatingBar)findViewById(R.id.ratingBar);
        OverallRating.setRating(3);
    }

    private class SendPubTask extends AsyncTask<Pub, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            // get the listview
//            expListView = (ExpandableListView) findViewById(R.id.lvExp);
//
//            listDataHeader = new ArrayList<String>();
//            listDataChild = new HashMap<String, List<String>>();
//        }

        @Override
        protected Void doInBackground(Pub... pub) {
            // Create URL
            final int idToDelete = pub[0].getId();
            final String targetURLString = pubsURL;

            URL targetURL = null;
            try {
                targetURL = new URL(targetURLString);
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

            // Parse the pub to send into JSON
            pubJSON = new JSONObject();
            try {
                pubJSON.put("name", pub[0].getName());
                pubJSON.put("street", pub[0].getStreet());
                pubJSON.put("city", pub[0].getCity());
                pubJSON.put("overall", pub[0].getOverall().ordinal()+1);
                pubJSON.put("design", 0.0f);
                pubJSON.put("design_description", pub[0].getDesignDescription());
                pubJSON.put("atmosphere", 0.0f);
                pubJSON.put("atmosphere_description", pub[0].getAtmosphereDescription());
            } catch (JSONException e) { }

            try {
                if(myConnection == null)
                    throw new IOException();
                myConnection.setRequestProperty("User-Agent", "beerdiary");
                myConnection.setRequestProperty("Authorization", "Token b97dcb9174dcbf567bb7fb7b523124755d0a14ea");
                myConnection.setRequestProperty("Content-Type", "application/json");
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setDoOutput(true);
                // TODO add a switch? to aither POST or PUT depending on adding/editing a pub
                myConnection.setRequestMethod("POST");

                OutputStreamWriter JSONWriter = new OutputStreamWriter(myConnection.getOutputStream());
                JSONWriter.write(pubJSON.toString());
                JSONWriter.flush();

                myConnection.connect();

                int response = myConnection.getResponseCode();

                // 201 - only for POST! (chyba, do weryfikacji u Pietra)
                if(response != 201) {
                    final String ResponseCode = String.valueOf(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(ModifyPubScreen.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Error code from server: " + ResponseCode);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                }

                myConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

//        @Override
//        protected void onPostExecute(Void result) { }
    }

    public void saveButtonOnClick(View v) {
        NameText       = (EditText)findViewById(R.id.editTextName);
        StreetText     = (EditText)findViewById(R.id.editTextStreet);
        CityText       = (EditText)findViewById(R.id.editTextCity);
        DesignText     = (EditText)findViewById(R.id.editTextDesignDesc);
        AtmosphereText = (EditText)findViewById(R.id.editTextAtmosphereDesc);
        OverallRating  = (RatingBar)findViewById(R.id.ratingBar);

        Name           = NameText.getText().toString();
        Street         = StreetText.getText().toString();
        City           = CityText.getText().toString();
        DesignDesc     = DesignText.getText().toString();
        AtmosphereDesc = AtmosphereText.getText().toString();
        OverallInt     = (int)OverallRating.getRating();
        Overall        = Rating.values()[Math.max(OverallInt-1, 0)];

        Pub newPub = new Pub(pubId, Name, Street, City, Overall, 0.0f, DesignDesc, 0.0f, AtmosphereDesc);

        new SendPubTask().execute(newPub);

        this.onBackPressed();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
