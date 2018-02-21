package ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.schenk.matthias.schafkopfcalculator.R;

import java.util.ArrayList;

import game.Game;
import game.GameController;
import game.GameMode;
import game.GameSettings;
import ui.custom.controls.SchafkopfButton;
import ui.custom.SchafkopfFragment;
import ui.interfaces.IGameSettingsFragmentListener;


/**
 * Created by Matthias on 11.01.2018.
 */

public class GameSetupFragment extends SchafkopfFragment {

    private EditText txtNamePlayer1, txtNamePlayer2, txtNamePlayer3, txtNamePlayer4;
    private EditText edtNormalGame, edtSolo, edtSchneider, edtSchwarz, edtLauf;
    private ArrayList<EditText> nameFileds = new ArrayList<>();
    private SchafkopfButton btnCreateNewGame;
    private ArrayList<IGameSettingsFragmentListener> lstGameSettingsListeners = new ArrayList<>();
    private CheckBox cbxNormalGame, cbxSolo;
    private boolean settingsValid = true;
    private GameSettings settings = new GameSettings(5, 5, 5);
    private ScrollView scrollViewMain;
    private boolean uiCreated = false;
    private LinearLayout layPlayers;
    private LinearLayout layGames;
    private LinearLayout layValues;


    public GameSetupFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findControls(view);
        listeners();
        uiCreated = true;
        init();
    }

    private void init() {
        cbxNormalGame.setChecked(true);
        cbxSolo.setChecked(true);

        Game game = GameController.getInstance().getActiveGame();
        if(game != null) update(game);
    }

    private void listeners() {
        btnCreateNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getSchafkopfActivity().isGameAvailable()){
                    openConfirmationDialog();
                }else{
                    createNewGame();
                }
            }
        });

        cbxNormalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbxNormalGame.setChecked(true);
            }
        });

        cbxSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbxSolo.setChecked(true);
            }
        });
    }

    private void openConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.are_u_sure);
        builder.setMessage(R.string.override_game);

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                createNewGame();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void findControls(View view) {
        nameFileds.clear();

        txtNamePlayer1 = (EditText) view.findViewById(R.id.edt_player_1);
        nameFileds.add(txtNamePlayer1);
        txtNamePlayer2 = (EditText) view.findViewById(R.id.edt_player_2);
        nameFileds.add(txtNamePlayer2);
        txtNamePlayer3 = (EditText) view.findViewById(R.id.edt_player_3);
        nameFileds.add(txtNamePlayer3);
        txtNamePlayer4 = (EditText) view.findViewById(R.id.edt_player_4);
        nameFileds.add(txtNamePlayer4);

        cbxNormalGame = (CheckBox) view.findViewById(R.id.cbx_normal_game);
        cbxSolo = (CheckBox) view.findViewById(R.id.cbx_solo);

        edtNormalGame = (EditText) view.findViewById(R.id.edt_normal_game);
        edtSolo = (EditText) view.findViewById(R.id.edt_solo);

        edtSchneider = (EditText) view.findViewById(R.id.edt_schneider);
        edtSchwarz = (EditText) view.findViewById(R.id.edt_schwarz);
        edtLauf = (EditText) view.findViewById(R.id.edt_lauf);

        btnCreateNewGame = (SchafkopfButton) view.findViewById(R.id.button_create_new_game);

        scrollViewMain = (ScrollView) view.findViewById(R.id.scrl_main);

        layPlayers = (LinearLayout) view.findViewById(R.id.lay_edit_payer_names);
        layGames = (LinearLayout) view.findViewById(R.id.lay_select_games);
        layValues = (LinearLayout) view.findViewById(R.id.lay_select_global_values);
    }

    private void createNewGame() {
        settingsValid = true;

        int valSchneider = checkValidityAndGetIntValue(edtSchneider.getText().toString());
        int valSchwarz = checkValidityAndGetIntValue(edtSchwarz.getText().toString());
        int valLauf = checkValidityAndGetIntValue(edtLauf.getText().toString());

        if(!settingsValid) return;

        settings = new GameSettings(valSchneider, valSchwarz, valLauf);
        settings.setLstPlayerNames(getPlayerNames(settings.getNumPlayers()));
        settings.setGameModes(getGameModes());

        onGameSettingsConfirmed(settings);
    }

    private int checkValidityAndGetIntValue(String s) {
        if (isNumeric(s)) return Integer.parseInt(s);

        getSchafkopfActivity().showToast("Zahl " + s + "ist ungültig!");
        settingsValid = false;

        return -1;
    }

    public boolean isNumeric(String str) {
        try {
            int i = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    private ArrayList<GameMode> getGameModes() {
        GameController controller = GameController.getInstance();
        ArrayList<GameMode> modes = new ArrayList<>();

        for (String id : controller.getHmAvailableModes().keySet()) {
            switch (id) {
                case GameController.ID_GAME_MODE_DEFAULT:
                    GameMode modeDefault = controller.getHmAvailableModes().get(GameController.ID_GAME_MODE_DEFAULT);
                    modeDefault.setValue(checkValidityAndGetIntValue(edtNormalGame.getText().toString()));
                    modes.add(modeDefault);
                    break;
                case GameController.ID_GAME_MODE_SOLO:
                    GameMode modeSolo = controller.getHmAvailableModes().get(GameController.ID_GAME_MODE_SOLO);
                    modeSolo.setValue(checkValidityAndGetIntValue(edtSolo.getText().toString()));
                    modes.add(modeSolo);
                    break;
            }
        }

        return modes;
    }

    private void onGameSettingsConfirmed(GameSettings settings) {
        for (IGameSettingsFragmentListener listener : lstGameSettingsListeners) {
            listener.onCreateNewGame(settings);
        }
    }

    private String[] getPlayerNames(int numPlayers) {
        String[] names = new String[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            names[i] = "Spieler " + (i+1);
        }

        for (int i = 0; i < names.length; i++) {
            if (i >= numPlayers) continue;
            if (nameFileds.get(i) != null) {
                String name = nameFileds.get(i).getText().toString();
                if(!name.equals("")) names[i] = name;
            }
        }

        return names;
    }

    public void addGameSettingsListener(IGameSettingsFragmentListener listener) {
        lstGameSettingsListeners.add(listener);
    }

    public void update(Game game) {
        if(!uiCreated) return;

        GameSettings settings = game.getSettings();

        for (int i = 0; i < settings.getNumPlayers(); i++) {
            nameFileds.get(i).setText(settings.getLstPlayerNames()[i]);
        }

        GameMode modeDefault = settings.getGameMode(GameController.ID_GAME_MODE_DEFAULT);
        GameMode modeSolo = settings.getGameMode(GameController.ID_GAME_MODE_SOLO);

        if(modeDefault != null) edtNormalGame.setText("" + modeDefault.getValue());
        if(modeSolo != null) edtSolo.setText("" + modeSolo.getValue());

        edtSchneider.setText("" + settings.getValueSchneider());
        edtSchwarz.setText("" + settings.getValueSchwarz());
        edtLauf.setText("" + settings.getValueLauf());
    }
}
