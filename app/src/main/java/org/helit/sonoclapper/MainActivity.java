package org.helit.sonoclapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private int scene_count = 0;
    private int view_count = 0;
    private int take_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void num_increment(View b) {

        switch(b.getId())
        {
            case R.id.button2:
                scene_count +=1;
                break;
            case R.id.button4:
                view_count += 1;
                break;
            case R.id.button6:
                take_count += 1;
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        //b.setEnabled(false);

        Toast bread = Toast.makeText(getApplicationContext(), "Increment!", Toast.LENGTH_SHORT);
        bread.show();
    }

    public void num_decrement(View b) {

        switch(b.getId())
        {
            case R.id.button1:
                scene_count -=1;
                break;
            case R.id.button3:
                view_count -= 1;
                break;
            case R.id.button5:
                take_count -= 1;
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        Toast bread = Toast.makeText(getApplicationContext(), "Decrement!", Toast.LENGTH_SHORT);
        bread.show();


    }
}


