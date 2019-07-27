package ui.custom.controls.fw;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.schenk.matthias.schafkopfcalculator.R;

/**
 * Created by Matthias on 11.02.2018.
 */

public class SchafkopfButton extends AppCompatButton {

    private Context context;
    private int colorBg, colorTxt;
    float alphaDisabled;

    public SchafkopfButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        colorBg = context.getResources().getColor(R.color.colorPrimary);
        colorTxt = context.getResources().getColor(R.color.colorButtonText);
        alphaDisabled = (float) 0.5;

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 16 );
        shape.setColor(colorBg);

        setBackground(shape);
        //setBackgroundColor(colorBg);
        setTextColor(colorTxt);

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? 1 : alphaDisabled);
    }

    public void setColorBg(int colorBg) {
        this.colorBg = colorBg;
        setBackgroundColor(colorBg);
    }
}
