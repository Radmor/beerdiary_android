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

/** klasa ekranu modyfikowania danych browaru
 */

public class ModifyBreweryScreen extends AppCompatActivity {

    private String User_Agent = "beerdiary";
    private JsonReader jsonReader = null;
    String breweriesURL = "http://164.132.101.153:8000/api/breweries/";

    boolean addingBrewery = true;

    Brewery myBrewery;
    int breweryId = -1;

    EditText NameText;
    EditText NoteText;
    RatingBar OverallRating;

    String Name;
    String Note;
    int OverallInt;
    Rating Overall;

    JSONObject breweryJSON;

    /** metoda obsługująca stworzenie ekranu modyfikowania browaru
     * @param savedInstanceState     zachowany stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Browary");
        setContentView(R.layout.modify_brewery);

        NameText       = (EditText)findViewById(R.id.editTextName);
        NoteText     = (EditText)findViewById(R.id.editTextNote);
        OverallRating  = (RatingBar)findViewById(R.id.ratingBar);

        myBrewery = (Brewery) getIntent().getSerializableExtra("Brewery");
        breweryId = myBrewery.getId();

        NameText.setText(myBrewery.getName());
        NoteText.setText(myBrewery.getNote());
        OverallRating.setRating(myBrewery.getOverall().ordinal()+1);
    }

    /** klasa stanowiąca zadanie wysłania zmienionych danych browaru na serwer
     */
    private class SendBreweryTask extends AsyncTask<Brewery, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            // get the listview
//            expListView = (ExpandableListView) findViewById(R.id.lvExp);
//
//            listDataHeader = new ArrayList<String>();
//            listDataChild = new HashMap<String, List<String>>();
//        }

        /** metoda faktycznie wysyłająca dane browaru na serwer (działa w tle)
         * @param brewery           browar
         */
        @Override
        protected Void doInBackground(Brewery... brewery) {
            // Create URL
            final int idToDelete = brewery[0].getId();
            String targetURLnotFinal;
            if (breweryId == -1)
                targetURLnotFinal = breweriesURL;
            else
                targetURLnotFinal = breweriesURL + Integer.toString(breweryId) + "/";
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

            // Parse the brewery to send into JSON
            breweryJSON = new JSONObject();
            try {
                breweryJSON.put("name", brewery[0].getName());
                breweryJSON.put("note", brewery[0].getNote());
                breweryJSON.put("overall", brewery[0].getOverall().ordinal()+1);
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

                if(breweryId == -1)
                    HTTPMethod = "POST";
                else
                    HTTPMethod = "PUT";
                myConnection.setRequestMethod(HTTPMethod);

                OutputStreamWriter JSONWriter = new OutputStreamWriter(myConnection.getOutputStream());
                JSONWriter.write(breweryJSON.toString());
                JSONWriter.flush();

                myConnection.connect();

                int response = myConnection.getResponseCode();

                if(!((HTTPMethod == "POST" && response == 201) || ((HTTPMethod == "PUT" && response == 200)))) {
                    final String ResponseCode = String.valueOf(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(ModifyBreweryScreen.this).create();
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

    /** metoda obsługująca naciśnięcie przycisku zapisania browaru
     * @param v         widok
     */
    public void saveButtonOnClick(View v) {
        Name           = NameText.getText().toString();
        Note           = NoteText.getText().toString();
        OverallInt     = (int)OverallRating.getRating();
        Overall        = Rating.values()[Math.max(OverallInt-1, 0)];

        Brewery newBrewery = new Brewery(breweryId, Name, Overall, Note);

        new SendBreweryTask().execute(newBrewery);

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
