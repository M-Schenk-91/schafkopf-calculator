package ui.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.schenk.matthias.schafkopfcalculator.R;

import game.GameController;
import game.GameMode;
import game.GameRound;
import ui.custom.controls.fw.SchafkopfToggleButton;
import ui.interfaces.IRoundDialogListener;

public class ChooseScoreValuesFragment extends Fragment {

    public enum RoundResult {
        NONE, FREI, SCHNEIDER, SCHWARZ
    }

    private IRoundDialogListener listener;
    private SchafkopfToggleButton btnFrei;
    private SchafkopfToggleButton btnSchneider;
    private SchafkopfToggleButton btnSchwarz;
    private SchafkopfToggleButton btnJungfrau;


    public void setAddNewRoundDialogListener(IRoundDialogListener l){
        listener = l;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_choose_score_values, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateToggleButtonTexts();
    }

    private void updateToggleButtonTexts() {
        btnFrei.setText(getResources().getString(R.string.txt_frei));
        btnFrei.setTextOn(getResources().getString(R.string.txt_frei));
        btnFrei.setTextOff(getResources().getString(R.string.txt_frei));

        btnSchneider.setText(getResources().getString(R.string.txt_schneider));
        btnSchneider.setTextOn(getResources().getString(R.string.txt_schneider));
        btnSchneider.setTextOff(getResources().getString(R.string.txt_schneider));

        btnSchwarz.setText(getResources().getString(R.string.txt_schwarz));
        btnSchwarz.setTextOn(getResources().getString(R.string.txt_schwarz));
        btnSchwarz.setTextOff(getResources().getString(R.string.txt_schwarz));

        btnJungfrau.setText(getResources().getString(R.string.txt_jungfrau));
        btnJungfrau.setTextOn(getResources().getString(R.string.txt_jungfrau));
        btnJungfrau.setTextOff(getResources().getString(R.string.txt_jungfrau));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        GameRound roundResult = (GameRound) bundle.getSerializable("roundResult");

        setupControls(view);

        GameMode mode = roundResult.getGameMode();
        setupBasedOnGameMode(mode);

        updateToggleButtonTexts();
    }

    private void setupBasedOnGameMode(GameMode mode) {

        switch (mode.getName()){
            case GameController.ID_GAME_MODE_RAMSCH:
                btnJungfrau.setVisibility(View.VISIBLE);

                btnFrei.setVisibility(View.GONE);
                btnSchneider.setVisibility(View.GONE);
                btnSchwarz.setVisibility(View.GONE);
                break;

            default:
                btnJungfrau.setVisibility(View.GONE);

                btnFrei.setVisibility(View.VISIBLE);
                btnSchneider.setVisibility(View.VISIBLE);
                btnSchwarz.setVisibility(View.VISIBLE);

                btnFrei.setChecked(true);
                break;
        }

    }

    private void setupControls(View view) {
        btnFrei = (SchafkopfToggleButton) view.findViewById(R.id.btn_frei);
        btnFrei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(RoundResult.FREI);
            }
        });

        btnSchneider = (SchafkopfToggleButton) view.findViewById(R.id.btn_schneider);
        btnSchneider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(RoundResult.SCHNEIDER);
            }
        });

        btnSchwarz = (SchafkopfToggleButton) view.findViewById(R.id.btn_schwarz);
        btnSchwarz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(RoundResult.SCHWARZ);
            }
        });

        btnJungfrau = (SchafkopfToggleButton) view.findViewById(R.id.btn_jungfrau);
        btnJungfrau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onJungfrauChanged(btnJungfrau.isChecked());
            }
        });

        final TextView lblLauf = (TextView) view.findViewById(R.id.lbl_lauf);

        SeekBar barLauf = (SeekBar) view.findViewById(R.id.sb_insert_lauf);
        barLauf.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                if(fromUser){
                    //1 Lauf gibt es nicht
                    if(progress != 1){
                        lblLauf.setText("" + progress);
                        listener.onLaufChanged(progress);
                    }else{
                        seekBar.setProgress(0);
                        lblLauf.setText("" + 0);
                        listener.onLaufChanged(0);
                    }
                }
            }
        });



    }

    private void select(RoundResult selected) {
        switch (selected){
            case FREI:
                btnFrei.setChecked(true);
                btnSchneider.setChecked(false);
                btnSchwarz.setChecked(false);
                break;

            case SCHNEIDER:
                btnFrei.setChecked(false);
                btnSchneider.setChecked(true);
                btnSchwarz.setChecked(false);
                break;

            case SCHWARZ:
                btnFrei.setChecked(false);
                btnSchneider.setChecked(false);
                btnSchwarz.setChecked(true);
                break;
        }

        listener.onRoundResultChanged(selected);

    }
}
