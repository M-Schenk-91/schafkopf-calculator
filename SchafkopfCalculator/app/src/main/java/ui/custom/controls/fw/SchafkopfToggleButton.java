package ui.custom.controls.fw;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.schenk.matthias.schafkopfcalculator.R;

/**
 * Created by Matthias on 11.02.2018.
 */

public class SchafkopfToggleButton extends ToggleButton {
    private Context context;
    private int colorBg, colorTxt, colorSelected;

    public SchafkopfToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        colorBg = context.getResources().getColor(R.color.colorPrimary);
        colorTxt = context.getResources().getColor(R.color.colorButtonText);
        colorSelected = context.getResources().getColor(R.color.colorToggleButtonSelected);

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 16 );
        shape.setColor(colorBg);

        setBackground(shape);
        setTextColor(colorTxt);


        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setBackgroundColor(isChecked ? colorSelected : colorBg);
            }
        });
    }
}
