package pl.poznan.put.cs.io.beerdiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void beerButtonOnClick(View v) {
        findViewById(R.id.PubButton).setVisibility(View.INVISIBLE);
    }

    public void addButtonOnClick(View v) {
        findViewById(R.id.PubButton).setVisibility(View.VISIBLE);
    }
}
