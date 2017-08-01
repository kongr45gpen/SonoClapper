package org.helit.sonoclapper;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


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


    }

    public void num_increment(View b) {
        EditText text;

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
        EditText text;

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

    public void go_button(View b) {
        playSound(507,44100);

    }



    /**
     * frequency generator
     * @param frequency ->hz
     * @param duration ->samples
     */
    private void playSound(double frequency, int duration) {
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        // Sine wave
        double[] mSound = new double[44100];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            if (i >= 40000) {
                mSound[i] *= -1/4100.0 * i + 441/41.0;
            }
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();

        mAudioTrack.write(mBuffer, 0, mSound.length);
        mAudioTrack.stop();
        mAudioTrack.release();

    }
}


