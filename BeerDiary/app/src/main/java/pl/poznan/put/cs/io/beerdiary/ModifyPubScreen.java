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
import android.widget.SeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/** klasa ekranu modyfikowania danych pubu
 */

public class ModifyPubScreen extends AppCompatActivity {

    private String User_Agent = "beerdiary";
    private JsonReader jsonReader = null;
    String pubsURL = "http://164.132.101.153:8000/api/pubs/";

    // robie dzis tylko dodawanie; docelowo ten ekran powinien wiedziec, czy dodajemy, czy edytujemy, i dodatkowo pamietac indeks edytowanego pubu.
    boolean addingPub = true;
    // TODO add PUT method and keep pub id in the class
    Pub myPub;
    int pubId = -1;

    EditText NameText;
    EditText StreetText;
    EditText CityText;
    EditText DesignText;
    EditText AtmosphereText;
    RatingBar OverallRating;
    SeekBar DesignRating;
    SeekBar AtmosphereRating;

    String Name;
    String Street;
    String City;
    String DesignDesc;
    String AtmosphereDesc;
    int OverallInt;
    Rating Overall;
    float Design;
    float Atmosphere;

    JSONObject pubJSON;

    /** metoda obsługująca stworzenie ekranu modyfikowania pubu
     * @param savedInstanceState     zachowany stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Puby");
        setContentView(R.layout.modify_pub);

        NameText       = (EditText)findViewById(R.id.editTextName);
        StreetText     = (EditText)findViewById(R.id.editTextStreet);
        CityText       = (EditText)findViewById(R.id.editTextCity);
        DesignText     = (EditText)findViewById(R.id.editTextDesignDesc);
        AtmosphereText = (EditText)findViewById(R.id.editTextAtmosphereDesc);
        OverallRating  = (RatingBar)findViewById(R.id.ratingBar);
        DesignRating   = (SeekBar)findViewById(R.id.seekBarDesign);
        AtmosphereRating   = (SeekBar)findViewById(R.id.seekBarAtmosphere);

        myPub = (Pub)getIntent().getSerializableExtra("Pub");
        pubId = myPub.getId();

        NameText.setText(myPub.getName());
        StreetText.setText(myPub.getStreet());
        CityText.setText(myPub.getCity());
        DesignText.setText(myPub.getDesignDescription());
        AtmosphereText.setText(myPub.getAtmosphereDescription());
        OverallRating.setRating(myPub.getOverall().ordinal()+1);

        DesignRating.setMax(100);
        DesignRating.setProgress(Math.round(100 * myPub.getDesign()));
        AtmosphereRating.setMax(100);
        AtmosphereRating.setProgress(Math.round(100 * myPub.getAtmosphere()));
    }

    /** klasa stanowiąca zadanie wysłania zmienionych danych pubu na serwer
     */
    private class SendPubTask extends AsyncTask<Pub, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            // get the listview
//            expListView = (ExpandableListView) findViewById(R.id.lvExp);
//
//            listDataHeader = new ArrayList<String>();
//            listDataChild = new HashMap<String, List<String>>();
//        }

        /** metoda faktycznie wysyłająca dane browaru na serwer (działa w tle)
         * @param pub           pub
         */
        @Override
        protected Void doInBackground(Pub... pub) {
            // Create URL
            final int idToDelete = pub[0].getId();
            String targetURLnotFinal;
            if (pubId == -1)
                targetURLnotFinal = pubsURL;
            else
                targetURLnotFinal = pubsURL + Integer.toString(pubId) + "/";
            final String targetURLString = targetURLnotFinal;

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
                pubJSON.put("design", pub[0].getDesign());
                pubJSON.put("design_description", pub[0].getDesignDescription());
                pubJSON.put("atmosphere", pub[0].getAtmosphere());
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

                String HTTPMethod;

                if(pubId == -1)
                    HTTPMethod = "POST";
                else
                    HTTPMethod = "PUT";
                myConnection.setRequestMethod(HTTPMethod);

                OutputStreamWriter JSONWriter = new OutputStreamWriter(myConnection.getOutputStream());
                JSONWriter.write(pubJSON.toString());
                JSONWriter.flush();

                myConnection.connect();

                int response = myConnection.getResponseCode();

                if(!((HTTPMethod == "POST" && response == 201) || ((HTTPMethod == "PUT" && response == 200)))) {
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

    /** metoda obsługująca naciśnięcie przycisku zapisania pubu
     * @param v         widok
     */
    public void saveButtonOnClick(View v) {
        Name           = NameText.getText().toString();
        Street         = StreetText.getText().toString();
        City           = CityText.getText().toString();
        DesignDesc     = DesignText.getText().toString();
        AtmosphereDesc = AtmosphereText.getText().toString();
        OverallInt     = (int)OverallRating.getRating();
        Overall        = Rating.values()[Math.max(OverallInt-1, 0)];
        Design         = ((float)DesignRating.getProgress() / 100);
        Atmosphere     = ((float)AtmosphereRating.getProgress() / 100);

        Pub newPub = new Pub(pubId, Name, Street, City, Overall, Design, DesignDesc, Atmosphere, AtmosphereDesc);

        new SendPubTask().execute(newPub);

        // TODO Alert with HTTP error code doesn't show when switching activity
        this.onBackPressed();
    }

    /** metoda obłsugująca zdarzenie naciśnięcia klawisza wstecz
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
