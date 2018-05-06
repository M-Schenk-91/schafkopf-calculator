package ui.custom.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.schenk.matthias.schafkopfcalculator.R;

import ui.custom.SchafkopfActivity;
import ui.custom.controls.fw.SchafkopfButton;

public class DoubleUpButton extends LinearLayout {

    private SchafkopfButton btnDoubleUp;
    private ImageButton btnClearDoubleUps;
    private SchafkopfActivity activity;

    private int doubleUps = 0;
    private int maxDoubleUps = Integer.MAX_VALUE;


    public DoubleUpButton(Context context) {
        super(context);
        activity = (SchafkopfActivity) context;

        initializeViews(context);
    }


    public DoubleUpButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (SchafkopfActivity) context;

        initializeViews(context);
    }

    public DoubleUpButton(Context context,
                       AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        activity = (SchafkopfActivity) context;

        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_double_up_button, this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnDoubleUp = (SchafkopfButton) this
                .findViewById(R.id.btn_double_up);

        btnClearDoubleUps = (ImageButton) this
                .findViewById(R.id.btn_clear_double_ups);

        btnClearDoubleUps.setBackgroundColor(activity.getResources().getColor(R.color.colorNegative));
        btnClearDoubleUps.setEnabled(false);

        btnDoubleUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doubleUp();
                handleUIState();
            }
        });

        btnClearDoubleUps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDoubleUps();
            }
        });
    }

    public void clearDoubleUps() {
        doubleUps = 0;
        handleUIState();
    }

    private void doubleUp() {

        if (doubleUps < maxDoubleUps){
            activity.vibrate(50);
            doubleUps++;
        }
    }

    private void handleUIState() {
        if (doubleUps == 0) {
            btnDoubleUp.setText("Doppeln");
            btnClearDoubleUps.setEnabled(false);
            btnClearDoubleUps.setVisibility(View.GONE);
        } else {
            btnDoubleUp.setText("Gedoppelt:\n" + doubleUps + " mal");
            btnClearDoubleUps.setEnabled(true);
            btnClearDoubleUps.setVisibility(View.VISIBLE);
        }
    }

    public int getDoubleUps() {
        return doubleUps;
    }

    public void setMaxDoubleUps(int maxDoubleUps) {
        this.maxDoubleUps = maxDoubleUps;
    }
}
