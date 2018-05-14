package ui.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schenk.matthias.schafkopfcalculator.R;

import game.GameController;
import game.GameMode;
import ui.custom.controls.fw.SchafkopfButton;
import ui.interfaces.IRoundDialogListener;

public class ChooseGameModeFragment extends Fragment {

    private IRoundDialogListener listener;

    public void addRoundDialogListener(IRoundDialogListener l){
        listener = l;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_choose_game_mode, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SchafkopfButton btnNormalGame = (SchafkopfButton) view.findViewById(R.id.btn_normal_game);
        btnNormalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameMode mode = GameController.getInstance().getActiveGame().getSettings().getGameMode(GameController.ID_GAME_MODE_DEFAULT);
                listener.onGameModeChanged(mode);
                listener.onPhaseCompleted(AddNewRoundDialogFragment.PHASE_CHOOSE_GAME_MODE, true);
            }
        });

        SchafkopfButton btnSolo = (SchafkopfButton) view.findViewById(R.id.btn_solo);
        btnSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameMode mode = GameController.getInstance().getActiveGame().getSettings().getGameMode(GameController.ID_GAME_MODE_SOLO);
                listener.onGameModeChanged(mode);
                listener.onPhaseCompleted(AddNewRoundDialogFragment.PHASE_CHOOSE_GAME_MODE, true);
            }
        });

        SchafkopfButton btnWenz = (SchafkopfButton) view.findViewById(R.id.btn_wenz);
        btnWenz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameMode mode = GameController.getInstance().getActiveGame().getSettings().getGameMode(GameController.ID_GAME_MODE_WENZ);
                listener.onGameModeChanged(mode);
                listener.onPhaseCompleted(AddNewRoundDialogFragment.PHASE_CHOOSE_GAME_MODE, true);
            }
        });

        SchafkopfButton btnRamsch = (SchafkopfButton) view.findViewById(R.id.btn_ramsch);
        btnRamsch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameMode mode = GameController.getInstance().getActiveGame().getSettings().getGameMode(GameController.ID_GAME_MODE_RAMSCH);
                listener.onGameModeChanged(mode);
                listener.onPhaseCompleted(AddNewRoundDialogFragment.PHASE_CHOOSE_GAME_MODE, true);
            }
        });

        SchafkopfButton btnCustom = (SchafkopfButton) view.findViewById(R.id.btn_custom_input);
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameMode mode = GameController.getInstance().getActiveGame().getSettings().getGameMode(GameController.ID_GAME_MODE_CUSTOM);
                listener.onGameModeChanged(mode);
                listener.onPhaseCompleted(AddNewRoundDialogFragment.PHASE_CHOOSE_GAME_MODE, true);
            }
        });
    }
}
