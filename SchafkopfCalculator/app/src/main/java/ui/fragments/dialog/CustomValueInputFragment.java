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
import ui.custom.controls.fw.SchafkopfToggleButton;

public class CustomValueInputFragment extends Fragment {

    private AddNewRoundDialogFragment addNewRoundDialogListener;

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
        GameRound roundResult = (GameRound) bundle.getSerializable("roundResult");
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        activeGame = GameController.getInstance().getActiveGame();

        txtInfo.setText(getString(R.string.custom_input_info));

        txtPlayer1.setText(activeGame.getLstPlayers().get(0).getName() + ":");
        txtPlayer2.setText(activeGame.getLstPlayers().get(1).getName() + ":");
        txtPlayer3.setText(activeGame.getLstPlayers().get(2).getName() + ":");
        txtPlayer4.setText(activeGame.getLstPlayers().get(3).getName() + ":");

        edtPlayer1.setText("" + 0);
        edtPlayer2.setText("" + 0);
        edtPlayer3.setText("" + 0);
        edtPlayer4.setText("" + 0);
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

                double score1 = Double.parseDouble(edtPlayer1.getText().toString());
                double score2 = Double.parseDouble(edtPlayer2.getText().toString());
                double score3 = Double.parseDouble(edtPlayer3.getText().toString());
                double score4 = Double.parseDouble(edtPlayer4.getText().toString());

                boolean isCorrect = score1 + score2 + score3 + score4 == 0;
                
                if(isCorrect){
                    txtInfo.setText(getString(R.string.custom_input_info_correct));
                }else{
                    txtInfo.setText(getString(R.string.custom_input_info_wrong));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        edtPlayer1.addTextChangedListener(watcher);
        edtPlayer2.addTextChangedListener(watcher);
        edtPlayer3.addTextChangedListener(watcher);
        edtPlayer4.addTextChangedListener(watcher);

    }

    public void setAddNewRoundDialogListener(AddNewRoundDialogFragment addNewRoundDialogListener) {
        this.addNewRoundDialogListener = addNewRoundDialogListener;
    }
}
