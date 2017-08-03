package org.helit.sonoclapper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText sceneText;
    EditText viewText;
    EditText takeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sceneText = (EditText) findViewById(R.id.scene_edit_text);
        viewText = (EditText) findViewById(R.id.view_edit_text);
        takeText = (EditText) findViewById(R.id.take_edit_text);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void num_increment(View b) {
        EditText text;

        switch (b.getId()) {
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
    }

    public void num_decrement(View b) {
        EditText text;

        switch (b.getId()) {
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
    }

    public void go_button(final View b) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final int clockMillis = sharedPref.getInt("clock_ms", 100);

        final ToneGenerator toneGenerator = new ToneGenerator(clockMillis);

        b.setEnabled(false);
        Thread t = new Thread(new Runnable() {
            public void run() {
                toneGenerator.produceClockPulses();
                toneGenerator.produceVersionPulse();
                toneGenerator.produceNumberPulse(Integer.valueOf(sceneText.getText().toString()));
                toneGenerator.produceNumberPulse(Integer.valueOf(viewText.getText().toString()));
                toneGenerator.produceNumberPulse(Integer.valueOf(takeText.getText().toString()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        b.setEnabled(true);
                    }
                });
            }
        });
        t.start();


    }
}


