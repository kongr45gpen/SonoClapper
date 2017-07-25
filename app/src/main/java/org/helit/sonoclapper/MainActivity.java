package org.helit.sonoclapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void grapseStart(View b) {
        //b.setEnabled(false);


        Toast bread = Toast.makeText(getApplicationContext(), "Congrats! You pressed the button!!!", Toast.LENGTH_SHORT);
        bread.show();
    }
}


