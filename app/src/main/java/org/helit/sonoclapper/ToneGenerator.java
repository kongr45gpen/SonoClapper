package org.helit.sonoclapper;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.zip.CRC32;

class ToneGenerator {
    public static final double CLOCK_FREQUENCY1 = 1077.39258;
    public static final double CLOCK_FREQUENCY2 = 1593.60352; // TODO: 1.5* harmonics?
    public static final double ZERO_FREQUENCY = 823; // will probably not be used
    private static final double[] FREQUENCIES = {1077.39258, 3187.20703, 3703.41797, 5297.02148, 6890.62500, 8484.22852, 10077.83203, 13781.25000};

    private static final int SAMPLE_RATE = 44100;

    private static final int RAMPDOWN_SAMPLES = 1000;
    private static final double RAMPDOWN_a = 0.008;
    private static final double RAMPDOWN_k = 1/(Math.exp(RAMPDOWN_SAMPLES * RAMPDOWN_a) - 1);

    private double defaultDuration = 0.1;

    private int mBufferSize; // minimum buffer size
    private AudioTrack mAudioTrack;

    private static final int version = 1;

    private ArrayList<Byte> providedData = new ArrayList<Byte>();

    public ToneGenerator(int clockMs) {
        defaultDuration = clockMs / 1000.0f; // duration is in seconds
    }

    public void start() {
        initialiseAudioTrack();
    }

    public void stop() {
        // Workaround for issue with inaudible last samples
        SystemClock.sleep(500);

        releaseAudioTrack();
    }

    public void produceClockPulses() {
        playSound(CLOCK_FREQUENCY1);
        playSound(CLOCK_FREQUENCY2);
        playSound(new double[]{CLOCK_FREQUENCY1, CLOCK_FREQUENCY2});
    }

    void produceChecksumPulse() {
        Log.e("MYCLASS", providedData.toString());
        CRC32 crc = new CRC32();
        for (Byte temp : providedData) {
            Log.e("MYCLASS", temp.toString());
            crc.update(temp.intValue());
        }

        long value = crc.getValue();

        // Get last 7 bits
        produceNumberPulse((int) (value & 0x7F));
    }

    public void produceVersionPulse() {
        providedData.add((byte) version);
        produceNumberPulse(version);
    }

    public void produceNumberPulse(int number) {
        providedData.add((byte) number);

        double[] frequencies = new double[8];

        int j = 0;
        for (int i = 0; i < 8; i++) {
            if (((number >> i) & 1) == 1) {
                frequencies[j++] = (FREQUENCIES[i]);
            }
        }
        if (number == 0) {
            frequencies[j++] = ZERO_FREQUENCY;
        }

        double[] freqs = new double[j];
        for (int i = 0; i < j; i++) {
            freqs[i] = frequencies[i];
        }

        playSound(freqs);
    }

    private void initialiseAudioTrack() {
        mBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
    }

    private void releaseAudioTrack() {
        mAudioTrack.release();
    }

    private void playSound(double frequency) {
        playSound(new double[] {frequency}, defaultDuration);
    }

    private void playSound(double frequency, double duration) {
        double[] frequencies = {frequency};
        playSound(frequencies, duration);
    }

    private void playSound(double[] frequencies) {
        playSound(frequencies, defaultDuration);
    }

    private void playSound(double[] frequencies, double duration) {
        int samples = (int) Math.ceil(duration * SAMPLE_RATE);

        if (samples < mBufferSize) {
            samples = mBufferSize;
        }

        double value;
        double frequencyFactor = 1/ (double) frequencies.length;
        short[] mBuffer = new short[samples];
        for (int i = 0; i < samples; i++) {
            value = 0;

            for (double frequency: frequencies) {
                // Sine wave
                value += Math.sin((2.0*Math.PI * i/(SAMPLE_RATE/frequency)));
            }
            value = frequencyFactor * rampDown(value, i, samples);

            mBuffer[i] = (short) (value*Short.MAX_VALUE);
        }

        mAudioTrack.play();
        mAudioTrack.write(mBuffer, 0, samples);
        mAudioTrack.stop();
    }



    private double rampDown(double sample, int i, int samples) {
        if (i + RAMPDOWN_SAMPLES > samples) {
            return sample * RAMPDOWN_k * (Math.exp(RAMPDOWN_a * (samples - i)) - 1.0);
        }

        return sample;
    }

    public static int getVersion() {
        return version;
    }
}
