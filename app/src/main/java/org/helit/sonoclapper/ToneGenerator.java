package org.helit.sonoclapper;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

class ToneGenerator {
    public static final int CLOCK_FREQUENCY1 = 637;
    public static final int CLOCK_FREQUENCY2 = 809;
    private static final int[] FREQUENCIES = {823, 1223, 1824, 3579, 6741, 8973, 10325, 12447};

    private static final int SAMPLE_RATE = 44100;

    private static final int RAMPDOWN_SAMPLES = 1000;
    private static final double RAMPDOWN_a = 0.008;
    private static final double RAMPDOWN_k = 1/(Math.exp(RAMPDOWN_SAMPLES * RAMPDOWN_a) - 1);

    private double defaultDuration = 0.1;

    private int mBufferSize; // minimum buffer size
    private AudioTrack mAudioTrack;

    private static final int version = 1;

    public ToneGenerator(int clockMs) {
        defaultDuration = clockMs / 1000.0f; // duration is in seconds
    }

    public void start() {
        initialiseAudioTrack();
    }

    public void stop() {
        releaseAudioTrack();
    }

    public void produceClockPulses() {
        playSound(CLOCK_FREQUENCY1);
        playSound(CLOCK_FREQUENCY2);
        playSound(new double[]{CLOCK_FREQUENCY1, CLOCK_FREQUENCY2});
    }

    public void produceVersionPulse() {
        produceNumberPulse(version);
    }

    public void produceNumberPulse(int number) {
        double[] frequencies = new double[8];

        int j = 0;
        for (int i = 0; i < 8; i++) {
            if (((number >> i) & 1) == 1) {
                frequencies[j++] = (FREQUENCIES[i]);
            }
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
}
