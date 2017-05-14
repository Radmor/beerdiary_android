package pl.poznan.put.cs.io.beerdiary;

import android.view.View;


/*
Interface klasy obslugujacej pobranie listy obiektów z serwera oraz wyswietlenie ich.
 */

interface AbstractScreen {
    void edit(View v, int breweryId);
    void deleteByGroupId(int groupId);
}