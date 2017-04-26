package pl.poznan.put.cs.io.beerdiary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;

public class ModifyPubScreen extends AppCompatActivity {

    private String User_Agent = "beerdiary";
    private JsonReader jsonReader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Puby");
        setContentView(R.layout.modify_pub);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
