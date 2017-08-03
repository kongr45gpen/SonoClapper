package org.helit.sonoclapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Locale;

/**
 * A preference with a slider
 * @todo See if it is necessary to store dialog state
 */
public class SliderPreference extends DialogPreference {
    private SeekBar clockBar;
    private EditText clockValue;

    private boolean stopFiringEvents = false;

    private static final int DEFAULT_VALUE = 100;
    private static final int MAX_VALUE = 1000;
    private static final int MIN_VALUE = 1;

    private static final double LOGSLIDER_a = 4; // empirically chosen variable
    private static final double LOGSLIDER_k = Math.exp(LOGSLIDER_a) - 1;

    private int currentValue;
    private int oldValue;

    public SliderPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_clock_setting);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        clockBar = v.findViewById(R.id.clockBar);
        clockValue = v.findViewById(R.id.clockValue);

        clockValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (stopFiringEvents) {
                    return;
                }

                try {
                    currentValue = Integer.valueOf(clockValue.getText().toString());
                } catch (NumberFormatException e) {
                    // ???
                    return;
                }

                if (currentValue > MAX_VALUE) {
                    currentValue = MAX_VALUE;
                    setTextValue();
                } else if (currentValue < MIN_VALUE) {
                    currentValue = MIN_VALUE;
                    setTextValue();
                }

                setProgressBarValue();
            }
        });
        clockBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (stopFiringEvents) {
                    return;
                }

                double seekValue = i / 1000.0;
                double value = (MAX_VALUE - 1.0)/LOGSLIDER_k * (Math.exp(LOGSLIDER_a * seekValue) - 1.0) + 1.0;
                currentValue = (int) Math.round(value);

                setTextValue();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        setValue(); // Update the text boxes when the view is created
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            persistInt(currentValue);

            if (callChangeListener(currentValue)) {
                // TODO: Put this in a proper place, so that it's called before the internal state
                // changed
                notifyChanged();
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            currentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            // Set default state from the XML attribute
            currentValue = (Integer) defaultValue;
            persistInt(currentValue);
        }
        oldValue = currentValue;
    }

    @Override
    public CharSequence getSummary() {
        return String.format(Locale.getDefault(), "%d ms", currentValue);
    }

    private void setValue() {
        setTextValue();
        setProgressBarValue();
    }

    private void setTextValue() {
        stopFiringEvents = true;
        clockValue.setText(Integer.toString(currentValue));
        stopFiringEvents = false;
    }

    private void setProgressBarValue(
    ) {
        stopFiringEvents = true;

        // Log slider:
        // y = (max-1)/k * (e^(ax)-1) + 1
        // x = 1/a * ln(k/999 * (y-1) + 1)
        // k = e^a - 1
        // where x is slider position (0,1)
        //   and y is actual position (1,max)
        double sliderValue = 1 / LOGSLIDER_a * Math.log(LOGSLIDER_k / (MAX_VALUE - 1.0) * (currentValue - 1.0) + 1.0);

        clockBar.setProgress((int) Math.round(sliderValue * 1000.0));
        stopFiringEvents = false;
    }
}