package pl.poznan.put.cs.io.beerdiary;

import android.content.DialogInterface;
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

/** klasa ekranu modyfikowania danych gatunku
 */

public class ModifyStyleScreen extends AppCompatActivity {

    private String User_Agent = "beerdiary";
    private JsonReader jsonReader = null;
    String stylesURL = "http://164.132.101.153:8000/api/styles/";

    boolean addingStyle = true;

    Style myStyle;
    int styleId = -1;

    EditText NameText;

    String Name;

    JSONObject styleJSON;

    /** metoda obsługująca stworzenie ekranu modyfikowania gatunku
     * @param savedInstanceState     zachowany stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Gatunki");
        setContentView(R.layout.modify_style);

        NameText       = (EditText)findViewById(R.id.editTextName);

        myStyle = (Style) getIntent().getSerializableExtra("Style");
        styleId = myStyle.getId();

        NameText.setText(myStyle.getName());
    }

    /** klasa stanowiąca zadanie wysłania zmienionych danych gatunku na serwer
     */
    private class SendStyleTask extends AsyncTask<Style, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            // get the listview
//            expListView = (ExpandableListView) findViewById(R.id.lvExp);
//
//            listDataHeader = new ArrayList<String>();
//            listDataChild = new HashMap<String, List<String>>();
//        }

        /** metoda faktycznie wysyłająca dane gatunku na serwer (działa w tle)
         * @param style           gatunek
         */
        @Override
        protected Void doInBackground(Style... style) {
            // Create URL
            final int idToSend = style[0].getId();
            String targetURLnotFinal;
            if (idToSend == -1)
                targetURLnotFinal = stylesURL;
            else
                targetURLnotFinal = stylesURL + Integer.toString(idToSend) + "/";
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

            // Parse the style to send into JSON
            styleJSON = new JSONObject();
            try {
                styleJSON.put("name", style[0].getName());
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

                if(idToSend == -1)
                    HTTPMethod = "POST";
                else
                    HTTPMethod = "PUT";
                myConnection.setRequestMethod(HTTPMethod);

                OutputStreamWriter JSONWriter = new OutputStreamWriter(myConnection.getOutputStream());
                JSONWriter.write(styleJSON.toString());
                JSONWriter.flush();

                myConnection.connect();

                int response = myConnection.getResponseCode();

                if(!((HTTPMethod == "POST" && response == 201) || ((HTTPMethod == "PUT" && response == 200)))) {
                    final String ResponseCode = String.valueOf(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(ModifyStyleScreen.this).create();
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

    /** metoda obsługująca naciśnięcie przycisku zapisania gatunku
     * @param v         widok
     */
    public void saveButtonOnClick(View v) {
        Name           = NameText.getText().toString();

        Style newStyle = new Style(styleId, Name);

        new SendStyleTask().execute(newStyle);

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
