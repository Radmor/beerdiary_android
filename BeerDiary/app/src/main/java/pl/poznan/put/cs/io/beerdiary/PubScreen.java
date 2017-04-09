package pl.poznan.put.cs.io.beerdiary;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

/**
 * Klasa obslugujaca pobranie listy pubow z serwera oraz wyswietlenie ich.
 */

public class PubScreen extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    android.content.Context context;
    String pubsURL = "http://164.132.101.153:8000/api/pubs/";

    /**
     * Metoda odczytujaca listę pubow w JSON i zwracajaca odpowiadajaca jej liste.
     * @param reader    JsonReader z lista
     * @return          Lista obiektow klasy Pub
     * @throws IOException  Wyjatek klasy JsonReader
     */
    private List<Pub> readPubArray(JsonReader reader) throws IOException {
        List<Pub> pubs  = new ArrayList<Pub>();

        reader.beginArray();
        while (reader.hasNext()) {
            pubs.add(readPub(reader));
        }
        reader.endArray();

        return pubs;
    }

    /**
     * Metoda odczytujaca pojedynczy pub w JSON i zwracajaca odpowiadajacy mu obiekt.
     * @param reader    JsonReader z obiektem pubu
     * @return          Obiekt klasy Pub
     * @throws IOException  Wyjatek klasy JsonReader
     */
    private Pub readPub(JsonReader reader) throws IOException{
        String pubName = "";
        String street = "";
        String city = "";
        Rating overall = Rating._1;
        float design = 0.0f;
        String designDescription = "";
        float atmosphere = 0.0f;
        String atmosphereDescription = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "name":
                    pubName = reader.nextString();
                    break;
                case "street":
                    street = reader.nextString();
                    break;
                case "city":
                    city = reader.nextString();
                    break;
                case "overall":
                    overall = Rating.values()[reader.nextInt() - 1];
                    break;
                case "design":
                    design = (float) reader.nextDouble();
                    break;
                case "design_description":
                    designDescription = reader.nextString();
                    break;
                case "atmosphere":
                    atmosphere = (float) reader.nextDouble();
                    break;
                case "atmosphere_description":
                    atmosphereDescription = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return new Pub(pubName, street, city, overall, design, designDescription, atmosphere, atmosphereDescription);
    }

    /**
     * Podklasa odpowiadajaca za asynchroniczne pobranie danych pubow z serwera i wyswietlenie ich po pobraniu.
     */
    private class GetPubsTask extends AsyncTask<Void, Void, Void> {
        /**
         * Przeladowana metoda wywolywana przed asynchronicznym przetwarzaniem; przygotowuje obiekty do pobrania i wyswietlenia.
         */
        @Override
        protected void onPreExecute() {
            // get the listview
            expListView = (ExpandableListView) findViewById(R.id.lvExp);

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
        }

        /**
         * Przeladowana metoda faktycznie pobierajaca dane z serwera, a nastepnie przetwarzajaca JSONa do obiektow klasy Pub.
         * @param arg0  sztuczny argument, potrzebny do przeladowania odpowiedniej metody z nadklasy
         * @return      sztyczna wartosc null, jak wyzej
         */
        @Override
        protected Void doInBackground(Void... arg0) {// Create URL
            URL targetURL = null;
            try {
                targetURL = new URL(pubsURL);
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

                int response = myConnection.getResponseCode();

                if(response == 200) {
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");


                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    // preparing list data
                    prepareListData(jsonReader);
                }
                else {
                    String ResponseCode = String.valueOf(response);

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("WProblem z połączeniem. Protokół http zwrócił kod " + ResponseCode);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "O nie!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                myConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Przeladowana metoda wyswietlajaca pobrane i przetworzone dane.
         * @param result    sztuczny argument, potrzebny do przeladowania odpowiedniej metody z nadklasy
         */
        @Override
        protected void onPostExecute(Void result) {

            listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);
        }
    }

    /**
     * Przeladowana metoda odpowiadajaca za zaladowanie ekranu wyswietlania pubow
     * @param savedInstanceState    stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pub_menu);

        context = this;
        new GetPubsTask().execute();
    }

    /**
     * Metoda przetwarzajaca sciagniety JSON na liste obiektow klasy PUB, a nastepnie przygotowujaca je do wyswietlenia.
     * @param reader
     * @throws IOException
     */
    private void prepareListData(JsonReader reader) throws IOException {
        List<Pub> pubList = readPubArray(reader);

        for (int i = 0; i < pubList.size(); i++) {
            listDataHeader.add(pubList.get(i).getName());
            List<String> child = new ArrayList<String>();

            child.add("Miasto:         " + pubList.get(i).getCity());
            child.add("Ulica:          " + pubList.get(i).getStreet());
            child.add("Ocena:          " + String.valueOf(pubList.get(i).getOverall().ordinal() + 1));
            child.add("Wystrój:        " + pubList.get(i).getDesign());
            child.add("Atmosfera:      " + pubList.get(i).getAtmosphere());

            listDataChild.put(listDataHeader.get(i), child); // Header, Child data
        }
    }

    /**
     * Przeladowana metoda zmieniajaca domyslna, brzydka animacje.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}