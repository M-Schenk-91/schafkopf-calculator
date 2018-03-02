package ui.fragments.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.schenk.matthias.schafkopfcalculator.R;

import java.util.ArrayList;
import java.util.HashMap;

import game.Game;
import game.GameController;
import game.GameMode;
import game.GameRound;
import ui.custom.controls.SchafkopfButton;
import ui.custom.controls.SchafkopfToggleButton;
import ui.FragmentController;
import ui.interfaces.IRoundDialogListener;

/**
 * Created by Matthias on 26.01.2018.
 */

public class AddNewRoundDialogFragment extends DialogFragment implements IRoundDialogListener {

    private static final int PHASE_CHOOSE_GAME_MODE = 0;
    private static final int PHASE_CHOOSE_WINNER = 1;
    private static final int PHASE_CHOOSE_SCORE_VARIABLES = 2;

    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    private int phase = PHASE_CHOOSE_GAME_MODE;
    private SchafkopfButton btnPositive;
    private SchafkopfButton btnNegative;

    private Fragment frChooseWinner = new ChooseWinnerFragment(), frChooseScoreValues = new ChooseScoreValuesFragment(), frChooseGameMode = new ChooseGameModeFragment();
    private HashMap<Integer, Fragment> fragments = new HashMap<>();

    private GameRound gameRoundResult;
    private static Game activeGame;

    public AddNewRoundDialogFragment() {

        ((ChooseGameModeFragment) frChooseGameMode).addRoundDialogListener(this);
        ((ChooseWinnerFragment) frChooseWinner).setRoundDialogListener(this);
        ((ChooseScoreValuesFragment) frChooseScoreValues).setAddNewRoundDialogListener(this);

        fragments.put(PHASE_CHOOSE_GAME_MODE, frChooseGameMode);
        fragments.put(PHASE_CHOOSE_WINNER, frChooseWinner);
        fragments.put(PHASE_CHOOSE_SCORE_VARIABLES, frChooseScoreValues);

        activeGame = GameController.getInstance().getActiveGame();
        gameRoundResult = new GameRound(activeGame);

    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        keepImmersiveState();
        return inflater.inflate(R.layout.fragment_dialog_add_round, container, false);
    }

    private void keepImmersiveState() {
        Dialog dialog = getDialog();
        final Window window = dialog.getWindow();
        final View decorView = window.getDecorView();

        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility());

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                wm.updateViewLayout(decorView, window.getAttributes());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setLayout(AddNewRoundDialogFragment.WIDTH, AddNewRoundDialogFragment.HEIGHT);

        phase = PHASE_CHOOSE_GAME_MODE;

        int multiplicator = getArguments().getInt("multiplicator");
        gameRoundResult.setMultiplicator(multiplicator);

        btnPositive = (SchafkopfButton) view.findViewById(R.id.btn_next);;
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPhase();
            }
        });

        btnNegative = (SchafkopfButton) view.findViewById(R.id.btn_back);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phase == PHASE_CHOOSE_GAME_MODE) {
                    AddNewRoundDialogFragment.this.getDialog().cancel();
                    return;
                }

                previousPhase();
            }
        });

        changePhase(phase, FragmentController.TRANSITION_NONE);
    }

    private void previousPhase() {
        phase--;
        changePhase(phase, FragmentController.TRANSITION_IN_LEFT_TO_RIGHT);
    }

    private void nextPhase() {
        if (phase == PHASE_CHOOSE_SCORE_VARIABLES) {
            GameController.getInstance().addGameRoundToActiveGame(gameRoundResult);
            getDialog().dismiss();
            phase = PHASE_CHOOSE_GAME_MODE;
            return;
        }

        phase++;
        changePhase(phase, FragmentController.TRANSITION_IN_RIGHT_TO_LEFT);
    }


    private void handleButtons(int phase, boolean newPhase) {
        switch (phase) {
            case PHASE_CHOOSE_GAME_MODE:
                btnPositive.setText(R.string.next);
                btnNegative.setText(R.string.cancel);
                btnPositive.setEnabled(false);
                break;
            case PHASE_CHOOSE_WINNER:
                btnPositive.setText(R.string.next);
                btnNegative.setText(R.string.back);
                btnPositive.setEnabled(false);

                if(newPhase){
                    //Handle condition for autoProceed (count of selected players in fragment)
                    int autoProceedCondition = 2;
                    if(gameRoundResult.getGameMode().isSolo()) autoProceedCondition = activeGame.getLstPlayers().size() - 1;
                    ((ChooseWinnerFragment) frChooseWinner).setProceedCondition(autoProceedCondition);
                }else{
                    handleProceedButtonOnWinnerSelection();
                }

                break;
            case PHASE_CHOOSE_SCORE_VARIABLES:
                btnPositive.setText(R.string.add);
                btnNegative.setText(R.string.back);
                btnPositive.setEnabled(true);
                break;
        }
    }



    private void handleProceedButtonOnWinnerSelection() {
        boolean enabled = true;
        int numWinners = GameRound.getNumWinners(gameRoundResult.getLstWinners());

        //no winner selected
        if(numWinners <= 0) enabled = false;
        //solo and multiple winners selected, but required num winners too low
        if(gameRoundResult.getGameMode().isSolo() && numWinners > 1) enabled = false;
        //normal game and only 1 winner selected
        if(!gameRoundResult.getGameMode().isSolo() && numWinners == 1) enabled = false;

        btnPositive.setEnabled(enabled);
    }

    private void changePhase(int phase, int transition) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if(transition != FragmentController.TRANSITION_NONE){
            int animExit = -1;

            if(transition == FragmentController.TRANSITION_IN_RIGHT_TO_LEFT ) animExit = R.anim.exit_to_left;
            if(transition == FragmentController.TRANSITION_IN_LEFT_TO_RIGHT ) animExit = R.anim.exit_to_right;

            transaction.setCustomAnimations(transition, animExit);
        }

        transaction.replace(R.id.content_dialog_addRound, fragments.get(phase)).commit();
        handleButtons(phase, true);
    }

    @Override
    public void onPhaseCompleted(int phase, boolean proceed) {

        if(phase == PHASE_CHOOSE_WINNER && !proceed){
            handleButtons(PHASE_CHOOSE_WINNER, false);
        }

        if(proceed){
            this.phase = phase;
            nextPhase();
        }
    }

    @Override
    public void onGameModeChanged(GameMode mode) {
        gameRoundResult.setGameMode(mode);
    }

    @Override
    public void onWinnersChanged(boolean[] winners) {
        gameRoundResult.setLstWinners(winners);
    }

    @Override
    public void onLaufChanged(int lauf) {
        gameRoundResult.setLauf(lauf);
    }

    @Override
    public void onRoundResultChanged(ChooseScoreValuesFragment.RoundResult result) {

        gameRoundResult.setSchneider(false);
        gameRoundResult.setSchwarz(false);

        switch (result){
            case SCHNEIDER:
                gameRoundResult.setSchneider(true);
            break;

            case SCHWARZ:
                gameRoundResult.setSchwarz(true);
                break;
        }
    }


    public static class ChooseGameModeFragment extends Fragment {

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
        }
    }



    public static class ChooseWinnerFragment extends Fragment {

        private IRoundDialogListener listener;
        private ArrayList<SchafkopfToggleButton> lstTogglesWinner = new ArrayList<>();
        private boolean[] winners = null;
        private int proceedCondition = 2;

        public void setRoundDialogListener(IRoundDialogListener l){
            listener = l;
        }
        public void setProceedCondition(int condition){
            proceedCondition = condition;
        }

        public boolean[] getWinners(){
            return winners;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_dialog_choose_winner, container, false);
        }

        @Override
        public void onResume() {
            super.onResume();
            updateToggleButtonTexts();
        }

        @Override
        public void onPause() {
            super.onPause();
            reset();
        }

        private void reset() {
            winners = null;
            for (SchafkopfToggleButton tgl: lstTogglesWinner) {
                tgl.setChecked(false);
            }

            lstTogglesWinner.clear();
        }

        private void updateToggleButtonTexts() {
            for (int i = 0; i < lstTogglesWinner.size(); i++) {

                if(i >= activeGame.getLstPlayers().size()) continue;

                lstTogglesWinner.get(i).setText(activeGame.getLstPlayers().get(i).getName());
                lstTogglesWinner.get(i).setTextOff(activeGame.getLstPlayers().get(i).getName());
                lstTogglesWinner.get(i).setTextOn(activeGame.getLstPlayers().get(i).getName());
            }
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            int numPlayers = GameController.getInstance().getActiveGame().getLstPlayers().size();
            winners = new boolean[numPlayers];

            for (int i = 0; i < numPlayers; i++) {
                int playerNum = i + 1;

                int id = getResources().getIdentifier("tgl_player_" + playerNum, "id", getActivity().getPackageName());
                lstTogglesWinner.add(((SchafkopfToggleButton) view.findViewById(id)));

                final int numToggle = i;

                lstTogglesWinner.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        winners[numToggle] = lstTogglesWinner.get(numToggle).isChecked();
                        listener.onWinnersChanged(winners);

                        boolean proceed = GameRound.getNumWinners(winners) == proceedCondition;
                        listener.onPhaseCompleted(PHASE_CHOOSE_WINNER, proceed);
                    }
                });
            }

            updateToggleButtonTexts();
        }
    }



    public static class ChooseScoreValuesFragment extends Fragment {

        public enum RoundResult {
            FREI, SCHNEIDER, SCHWARZ
        }

        private IRoundDialogListener listener;
        private SchafkopfToggleButton btnFrei;
        private SchafkopfToggleButton btnSchneider;
        private SchafkopfToggleButton btnSchwarz;

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
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            btnFrei = (SchafkopfToggleButton) view.findViewById(R.id.btn_frei);
            btnFrei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(RoundResult.FREI);
                }
            });

            btnFrei.setChecked(true);

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

            updateToggleButtonTexts();

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
                    listener.onRoundResultChanged(selected);

                    btnFrei.setChecked(true);
                    btnSchneider.setChecked(false);
                    btnSchwarz.setChecked(false);
                    break;
                case SCHNEIDER:
                    listener.onRoundResultChanged(selected);

                    btnFrei.setChecked(false);
                    btnSchneider.setChecked(true);
                    btnSchwarz.setChecked(false);
                    break;
                case SCHWARZ:
                    listener.onRoundResultChanged(selected);

                    btnFrei.setChecked(false);
                    btnSchneider.setChecked(false);
                    btnSchwarz.setChecked(true);
                    break;
            }
        }
    }
}
