package org.helit.sonoclapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TextView sceneText;
    TextView viewText;
    TextView takeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        sceneText = (TextView) findViewById(R.id.scene_edit_text);
        viewText = (TextView) findViewById(R.id.view_edit_text);
        takeText = (TextView) findViewById(R.id.take_edit_text);


    }

    public void num_increment(View b) {
        TextView text;

        switch(b.getId())
        {
            case R.id.button2:
                text = sceneText;
                break;
            case R.id.button4:
                text = viewText;
                break;
            case R.id.button6:
                text = takeText;
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        int number = Integer.valueOf(text.getText().toString()); // Get value of the textbox as a number
        number += 1;
        text.setText(Integer.toString(number)); // Set string value of the textbox



        Toast notify = Toast.makeText(getApplicationContext(), "Increment!", Toast.LENGTH_SHORT);
        notify.show();
    }

    public void num_decrement(View b) {
        TextView text;

        switch(b.getId())
        {
            case R.id.button1:
                text = sceneText;
                break;
            case R.id.button3:
                text = viewText;
                break;
            case R.id.button5:
                text = takeText;
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        int number = Integer.valueOf(text.getText().toString());
        number -= 1;
        text.setText(Integer.toString(number));



        Toast notify = Toast.makeText(getApplicationContext(), "Decrement!", Toast.LENGTH_SHORT);
        notify.show();


    }
}


