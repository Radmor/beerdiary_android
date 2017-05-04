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
 * Klasa obslugujaca pobranie listy gatunkow z serwera oraz wyswietlenie ich.
 */

public class StyleScreen extends AppCompatActivity {

    ExpandableListAdapterStyles listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    android.content.Context context;
    String stylesURL = "http://164.132.101.153:8000/api/styles/";
    private List<Style> styles;

    public void addButtonOnClick(View v) {
        Intent intent = new Intent(StyleScreen.this, ModifyStyleScreen.class);
        intent.putExtra("Style", new Style(-1, ""));
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void editStyle(View v, int styleId) {
        Intent intent = new Intent(StyleScreen.this, ModifyStyleScreen.class);
        intent.putExtra("Style", styles.get(styleId));
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void DeleteStyleByGroupId(int groupId) {
        Style styleToDelete = styles.get(groupId);
        new DeleteStyleAndRefreshTask().execute(styleToDelete);
    }

    /**
     * Metoda odczytujaca listę batunkow w JSON i zwracajaca odpowiadajaca jej liste.
     * @param reader    JsonReader z lista
     * @return          Lista obiektow klasy Style
     * @throws IOException  Wyjatek klasy JsonReader
     */
    private List<Style> readStyleArray(JsonReader reader) throws IOException {
        List<Style> styles  = new ArrayList<Style>();

        reader.beginArray();
        while (reader.hasNext()) {
            styles.add(readStyle(reader));
        }
        reader.endArray();

        return styles;
    }

    /**
     * Metoda odczytujaca pojedynczy gatunek w JSON i zwracajaca odpowiadajacy mu obiekt.
     * @param reader    JsonReader z obiektem gatunku
     * @return          Obiekt klasy Style
     * @throws IOException  Wyjatek klasy JsonReader
     */
    private Style readStyle(JsonReader reader) throws IOException{
        int id = -1;
        String styleName = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("name")) {
                styleName = reader.nextString();

            }  else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new Style(id, styleName);
    }

    /**
     * Podklasa odpowiadajaca za asynchroniczne pobranie danych gatunkow z serwera i wyswietlenie ich po pobraniu.
     */
    private class GetStylesTask extends AsyncTask<Void, Void, Void> {
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
         * Przeladowana metoda faktycznie pobierajaca dane z serwera, a nastepnie przetwarzajaca JSONa do obiektow klasy Style.
         * @param arg0  sztuczny argument, potrzebny do przeladowania odpowiedniej metody z nadklasy
         * @return      sztyczna wartosc null, jak wyzej
         */
        @Override
        protected Void doInBackground(Void... arg0) {// Create URL
            URL targetURL = null;
            try {
                targetURL = new URL(stylesURL);
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
                            AlertDialog alertDialog = new AlertDialog.Builder(StyleScreen.this).create();
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

            listAdapter = new ExpandableListAdapterStyles(context, listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);
        }
    }
    /**
     * Podklasa odpowiadajaca za asynchroniczne usuniecia danych jednego z gatunkow z serwera oraz odwiezenie widoku.
     */
    private class DeleteStyleAndRefreshTask extends AsyncTask<Style, Void, Void> {
//        /**
//         * Przeladowana metoda wywolywana przed asynchronicznym przetwarzaniem; przygotowuje obiekty do pobrania i wyswietlenia.
//         */
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
         * @param style obiekt klasy Style do usuniecia
         * @return      sztuczna wartosc null, jak wyzej
         */
        @Override
        protected Void doInBackground(Style... style) {// Create URL
            final int idToDelete = style[0].getId();
            final String targetURLString = stylesURL + String.valueOf(idToDelete) + "/";

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
                myConnection.setDoOutput(true);
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
                            AlertDialog alertDialog = new AlertDialog.Builder(StyleScreen.this).create();
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
            new GetStylesTask().execute();
        }
    }

    /**
     * Przeladowana metoda odpowiadajaca za zaladowanie ekranu wyswietlania gatunkowow
     * @param savedInstanceState    stan instancji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.style_menu);
        getSupportActionBar().setTitle("Gatunki");
        context = this;
    }

    /**
     * Metoda przetwarzajaca sciagniety JSON na liste obiektow klasy Style, a nastepnie przygotowujaca je do wyswietlenia.
     * @param reader
     * @throws IOException
     */
    private void prepareListData(JsonReader reader) throws IOException {
        List<Style> styleList = readStyleArray(reader);
        styles = styleList;

        expListView.setGroupIndicator(null);

        for (int i = 0; i < styleList.size(); i++) {
            listDataHeader.add(styleList.get(i).getName());
            List<String> child = new ArrayList<String>();

//            child.add("Miasto:         " + pubList.get(i).getCity());
//            child.add("Ulica:          " + pubList.get(i).getStreet());
//            child.add("Ocena:          " + String.valueOf(pubList.get(i).getOverall().ordinal() + 1));
//            child.add("Wystrój:        " + pubList.get(i).getDesign());
//            child.add("Atmosfera:      " + pubList.get(i).getAtmosphere());

            listDataChild.put(listDataHeader.get(i), child); // Header, Child data
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new GetStylesTask().execute();
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