package ui.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.schenk.matthias.schafkopfcalculator.R;
import game.Game;
import game.GameController;
import game.GameRound;
import ui.AppColors;
import ui.UiUtils;
import ui.interfaces.IRoundDialogListener;

public class CustomValueInputFragment extends Fragment {

    private IRoundDialogListener roundDialogListener;
    private GameRound gameRoundResult;

    private TextView txtInfo;

    private TextView txtPlayer1;
    private TextView txtPlayer2;
    private TextView txtPlayer3;
    private TextView txtPlayer4;

    private EditText edtPlayer1;
    private EditText edtPlayer2;
    private EditText edtPlayer3;
    private EditText edtPlayer4;
    private Game activeGame;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_custom_value_input, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        Bundle bundle = getArguments();
        this.gameRoundResult = (GameRound) bundle.getSerializable("roundResult");
    }

    @Override
    public void onResume() {
        super.onResume();
        resetViews();
        resetRound();
    }

    private void resetRound() {
        gameRoundResult.setLstWinners(new boolean[]{false, false, false, false});
        gameRoundResult.setSchneider(false);
        gameRoundResult.setSchwarz(false);
        gameRoundResult.setLauf(0);
        gameRoundResult.setJungfrau(false);
    }

    private void resetViews() {
        activeGame = GameController.getInstance().getActiveGame();

        txtPlayer1.setText(activeGame.getLstPlayers().get(0).getName() + ":");
        txtPlayer2.setText(activeGame.getLstPlayers().get(1).getName() + ":");
        txtPlayer3.setText(activeGame.getLstPlayers().get(2).getName() + ":");
        txtPlayer4.setText(activeGame.getLstPlayers().get(3).getName() + ":");

        edtPlayer1.setText("" + 0);
        edtPlayer2.setText("" + 0);
        edtPlayer3.setText("" + 0);
        edtPlayer4.setText("" + 0);

        txtInfo.setTextColor(AppColors.COLOR_NEUTRAL);
        txtInfo.setText(getString(R.string.custom_input_info));
    }

    private void findViews(View view) {
        txtInfo = (TextView) view.findViewById(R.id.txt_custom_input_info_label);

        txtPlayer1 = (TextView) view.findViewById(R.id.txt_custom_input_player_1);
        txtPlayer2 = (TextView) view.findViewById(R.id.txt_custom_input_player_2);
        txtPlayer3 = (TextView) view.findViewById(R.id.txt_custom_input_player_3);
        txtPlayer4 = (TextView) view.findViewById(R.id.txt_custom_input_player_4);

        edtPlayer1 = (EditText) view.findViewById(R.id.edt_custom_input_player_1);
        edtPlayer2 = (EditText) view.findViewById(R.id.edt_custom_input_player_2);
        edtPlayer3 = (EditText) view.findViewById(R.id.edt_custom_input_player_3);
        edtPlayer4 = (EditText) view.findViewById(R.id.edt_custom_input_player_4);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        edtPlayer1.addTextChangedListener(watcher);
        edtPlayer2.addTextChangedListener(watcher);
        edtPlayer3.addTextChangedListener(watcher);
        edtPlayer4.addTextChangedListener(watcher);
    }

    private void checkInput() {
        String txtEdt1 = edtPlayer1.getText().toString();
        String txtEdt2 = edtPlayer2.getText().toString();
        String txtEdt3 = edtPlayer3.getText().toString();
        String txtEdt4 = edtPlayer4.getText().toString();

        boolean isCorrect = UiUtils.isInteger(txtEdt1) && UiUtils.isInteger(txtEdt2) && UiUtils.isInteger(txtEdt3) && UiUtils.isInteger(txtEdt4);
        roundDialogListener.onProceedAllowedChanged(isCorrect);
        if(!isCorrect){
            txtInfo.setTextColor(AppColors.COLOR_NEUTRAL);
            txtInfo.setText(getString(R.string.custom_input_info));
            return;
        }

        int score1 = Integer.parseInt(txtEdt1);
        int score2 = Integer.parseInt(txtEdt2);
        int score3 = Integer.parseInt(txtEdt3);
        int score4 = Integer.parseInt(txtEdt4);

        isCorrect = !((score1 == 0) && (score2 == 0) &&(score3 == 0) &&(score4 == 0));
        roundDialogListener.onProceedAllowedChanged(isCorrect);

        if(!isCorrect){
            txtInfo.setTextColor(AppColors.COLOR_NEUTRAL);
            txtInfo.setText(getString(R.string.custom_input_info));
            return;
        }

        isCorrect = score1 + score2 + score3 + score4 == 0;

        if(isCorrect){
            txtInfo.setText(getString(R.string.custom_input_info_correct));
            txtInfo.setTextColor(AppColors.COLOR_POSITIVE);

            gameRoundResult.setLstWinners(new boolean[]{score1 > 0, score2 > 0, score3 > 0, score4 > 0});
            gameRoundResult.setLstScoreChangesPerPlayer(new int[]{score1, score2, score3, score4});

            roundDialogListener.onCustomRoundChanged(gameRoundResult);
        }else{
            txtInfo.setText(getString(R.string.custom_input_info_wrong));
            txtInfo.setTextColor(AppColors.COLOR_NEGATIVE);
        }

        roundDialogListener.onProceedAllowedChanged(isCorrect);
    }

    public void setAddNewRoundDialogListener(IRoundDialogListener roundDialogListener) {
        this.roundDialogListener = roundDialogListener;
    }
}
