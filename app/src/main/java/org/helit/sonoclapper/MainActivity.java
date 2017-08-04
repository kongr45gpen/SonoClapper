package org.helit.sonoclapper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText sceneText;
    EditText viewText;
    EditText takeText;

//    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sceneText = (EditText) findViewById(R.id.scene_edit_text);
        viewText = (EditText) findViewById(R.id.view_edit_text);
        takeText = (EditText) findViewById(R.id.take_edit_text);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                int number = Integer.parseInt(text);

                if (number < 0){
                    number = 0;
                    Toast.makeText(getApplicationContext(),"Number must be greater than 0!",Toast.LENGTH_LONG).show();
                }
                else if (number >= 256){
                    number = 255;
                    Toast.makeText(getApplicationContext(),"Number must be less than 256!",Toast.LENGTH_LONG).show();
                }

                String correctText = Integer.toString(number);

                if (!text.equals(correctText)) {
                    Editable newEditable = new SpannableStringBuilder(correctText);
                    editable.replace(0, editable.length(), newEditable);
                }
            }
        };

        sceneText.addTextChangedListener(textWatcher);
        viewText.addTextChangedListener(textWatcher);
        takeText.addTextChangedListener(textWatcher);

//        mPrefs = getSharedPreferences("SonoClapper", MODE_PRIVATE);
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

        final EditText take = takeText;

        b.setEnabled(false);
        Thread t = new Thread(new Runnable() {
            public void run() {
                toneGenerator.start();
                toneGenerator.produceClockPulses();
                toneGenerator.produceVersionPulse();
                toneGenerator.produceNumberPulse(Integer.valueOf(sceneText.getText().toString()));
                toneGenerator.produceNumberPulse(Integer.valueOf(viewText.getText().toString()));
                toneGenerator.produceNumberPulse(Integer.valueOf(takeText.getText().toString()));
                toneGenerator.stop();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        b.setEnabled(true);

                        final Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Increase the take number after some time
                                int takeNumber = Integer.valueOf(takeText.getText().toString());
                                take.setText(String.valueOf(takeNumber+1));
                            }
                        }, 1000);
                    }
                });
            }
        });
        t.start();


    }
}


