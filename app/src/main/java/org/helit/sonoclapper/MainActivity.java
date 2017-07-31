package org.helit.sonoclapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    int scene_count = 0;
    int view_count = 0;
    int take_count = 0;
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

        switch(b.getId())
        {
            case R.id.button2:
                scene_count += 1;
                sceneText.setText(Integer.toString(scene_count));
                break;
            case R.id.button4:
                view_count += 1;
                viewText.setText(Integer.toString(view_count));
                break;
            case R.id.button6:
                take_count += 1;
                takeText.setText(Integer.toString(take_count));
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }



        Toast notify = Toast.makeText(getApplicationContext(), "Increment!", Toast.LENGTH_SHORT);
        notify.show();
    }

    public void num_decrement(View b) {

        switch(b.getId())
        {
            case R.id.button1:
                scene_count -=1;
                sceneText.setText(Integer.toString(scene_count));
                break;
            case R.id.button3:
                view_count -= 1;
                viewText.setText(Integer.toString(view_count));
                break;
            case R.id.button5:
                take_count -= 1;
                takeText.setText(Integer.toString(take_count));
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }



        Toast notify = Toast.makeText(getApplicationContext(), "Decrement!", Toast.LENGTH_SHORT);
        notify.show();


    }
}


