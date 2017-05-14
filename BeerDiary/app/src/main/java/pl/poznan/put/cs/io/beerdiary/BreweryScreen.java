package pl.poznan.put.cs.io.beerdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.widget.ExpandableListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Klasa obslugujaca pobranie listy browarow z serwera oraz wyswietlenie ich.
 */

public class BreweryScreen extends AppCompatActivity implements AbstractScreen {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    android.content.Context context;
    String breweriesURL = "http://164.132.101.153:8000/api/breweries";
    private List<Brewery> breweries;

    /** metoda obsługująca zdarzenie kliknięcia przycisku dodawania browaru
     * @param v            widok
     */
    public void addButtonOnClick(View v) {
        Intent intent = new Intent(BreweryScreen.this, ModifyBreweryScreen.class);
        intent.putExtra("Brewery", new Brewery(-1, "", Rating._3, ""));
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /** metoda służąca do edycji browaru
     * @param v             widok
     * @param breweryId     id browaru
     */
    public void edit(View v, int breweryId) {
        Intent intent = new Intent(BreweryScreen.this, ModifyBreweryScreen.class);
        intent.putExtra("Brewery", breweries.get(breweryId));
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    /** metoda usuwająca browary według id grupy
     * @param groupId       id grupy
     */
    public void deleteByGroupId(int groupId) {
        Brewery breweryToDelete = breweries.get(groupId);
        new DeleteBreweryAndRefreshTask().execute(breweryToDelete);
    }

    /**
     * Metoda odczytujaca listę browarow w JSON i zwracajaca odpowiadajaca jej liste.
     * @param reader    JsonReader z lista
     * @return          Lista obiektow klasy Brewery
     * @throws IOException  Wyjatek klasy JsonReader
     */
    private List<Brewery> readBreweryArray(JsonReader reader) throws IOException {
        List<Brewery> breweries  = new ArrayList<Brewery>();

        reader.beginArray();
        while (reader.hasNext()) {
            breweries.add(readBrewery(reader));
        }
        reader.endArray();

        return breweries;
    }

    /**
     * Metoda odczytujaca pojedynczy browar w JSON i zwracajaca odpowiadajacy mu obiekt.
     * @param reader    JsonReader z obiektem browaru
     * @return          Obiekt klasy Brewery
     * @throws IOException  Wyjatek klasy JsonReader
     */
    private Brewery readBrewery(JsonReader reader) throws IOException{
        int id = -1;
        String breweryName = "";
        Rating overall = Rating._1;
        String note = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();

            } else if (name.equals("name")) {
                breweryName = reader.nextString();

            } else if (name.equals("overall")) {
                overall = Rating.values()[reader.nextInt() - 1];

            } else if (name.equals("note")) {
                note = reader.nextString();

            } else {
                reader.skipValue();

            }
        }
        reader.endObject();

        return new Brewery(id, breweryName, overall, note);
    }

    /**
     * Podklasa odpowiadajaca za asynchroniczne pobranie danych pubow z serwera i wyswietlenie ich po pobraniu.
     */
    private class GetBreweriesTask extends AsyncTask<Void, Void, Void> {
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
         * Przeladowana metoda faktycznie pobierajaca dane z serwera, a nastepnie przetwarzajaca JSONa do obiektow klasy Brewery.
         * @param arg0  sztuczny argument, potrzebny do przeladowania odpowiedniej metody z nadklasy
         * @return      sztyczna wartosc null, jak wyzej
         */
        @Override
        protected Void doInBackground(Void... arg0) {// Create URL
            URL targetURL = null;
            try {
                targetURL = new URL(breweriesURL);
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
                myConnection.setRequestProperty("Authorization", "Token b97dcb9174dcbf567bb7fb7b523124755d0a14ea");
                myConnection.connect();

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
                    final String ResponseCode = String.valueOf(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(BreweryScreen.this).create();
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

        /**
         * Przeladowana metoda wyswietlajaca pobrane i przetworzone dane.
         * @param result    sztuczny argument, potrzebny do przeladowania odpowiedniej metody z nadklasy
         */
        @Override
        protected void onPostExecute(Void result) {

            listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild, BreweryScreen.this);

            // setting list adapter
            expListView.setAdapter(listAdapter);
        }
    }
    /**
     * Podklasa odpowiadajaca za asynchroniczne usuniecia danych jednego z browarow z serwera oraz odwiezenie widoku.
     */
    private class DeleteBreweryAndRefreshTask extends AsyncTask<Brewery, Void, Void> {
        /**
         * Przeladowana metoda wywolywana przed asynchronicznym przetwarzaniem; przygotowuje obiekty do pobrania i wyswietlenia.
         */
//        @Override
//        protected void onPreExecute() {
//            // get the listview
//            expListView = (ExpandableListView) findViewById(R.id.lvExp);
//
//            listDataHeader = new ArrayList<String>();
//            listDataChild = new HashMap<String, List<String>>();
//        }

        /**
         * Przeladowana metoda faktycznie wysylajaca zadanie usuniecia do serwera
         * @param brewery   obiekt klasy Brewery do usuniecia
         * @return      sztuczna wartosc null, jak wyzej
         */
        @Override
        protected Void doInBackground(Brewery... brewery) {// Create URL
            final int idToDelete = brewery[0].getId();
            final String targetURLString = breweriesURL  + "/" + String.valueOf(idToDelete);

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

            try {
                if(myConnection == null)
                    throw new IOException();
                //myConnection.setDoOutput(true);
                myConnection.setRequestProperty("User-Agent", "beerdiary");
                myConnection.setRequestProperty("Authorization", "Token b97dcb9174dcbf567bb7fb7b523124755d0a14ea");
                myConnection.setRequestMethod("DELETE");
                myConnection.connect();

                int response = myConnection.getResponseCode();

                if(response != 204) {
                    final String ResponseCode = String.valueOf(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(BreweryScreen.this).create();
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

        /**
         * Przeladowana metoda wyswietlajaca pobrane i przetworzone dane.
         * @param result    sztuczny argument, potrzebny do przeladowania odpowiedniej metody z nadklasy
         */
        @Override
        protected void onPostExecute(Void result) {
            new GetBreweriesTask().execute();
        }
    }

    /**
     * Przeladowana metoda odpowiadajaca za zaladowanie ekranu wyswietlania browarow
     * @param savedInstanceState    stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_menu);
        getSupportActionBar().setTitle("Browary");
        context = this;
    }

    /**
     * Metoda przetwarzajaca sciagniety JSON na liste obiektow klasy BREWERY, a nastepnie przygotowujaca je do wyswietlenia.
     * @param reader
     * @throws IOException
     */
    private void prepareListData(JsonReader reader) throws IOException {
        List<Brewery> breweryList = readBreweryArray(reader);
        breweries = breweryList;

        for (int i = 0; i < breweryList.size(); i++) {
            listDataHeader.add(breweryList.get(i).getName());
            List<String> child = new ArrayList<String>();

            child.add("Nazwa:         " + breweryList.get(i).getName());
            child.add("Ocena:          " + String.valueOf(breweryList.get(i).getOverall().ordinal() + 1));
            child.add("Notatka:        " + breweryList.get(i).getNote());

            listDataChild.put(listDataHeader.get(i), child); // Header, Child data
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new GetBreweriesTask().execute();
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