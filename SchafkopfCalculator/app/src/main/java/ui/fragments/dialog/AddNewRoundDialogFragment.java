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
import ui.custom.controls.fw.SchafkopfButton;
import ui.custom.controls.fw.SchafkopfToggleButton;
import ui.FragmentController;
import ui.interfaces.IRoundDialogListener;

/**
 * Created by Matthias on 26.01.2018.
 */

public class AddNewRoundDialogFragment extends DialogFragment implements IRoundDialogListener {

    public static final int PHASE_CHOOSE_GAME_MODE = 0;
    public static final int PHASE_CHOOSE_WINNER = 1;
    public static final int PHASE_CHOOSE_SCORE_VARIABLES = 2;

    public static final int PHASE_CUSTOM_INPUT = 20;


    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    private int phase = PHASE_CHOOSE_GAME_MODE;
    private SchafkopfButton btnPositive;
    private SchafkopfButton btnNegative;

    private Fragment frChooseWinner = new ChooseWinnerFragment();
    private Fragment frChooseScoreValues = new ChooseScoreValuesFragment();
    private Fragment frChooseGameMode = new ChooseGameModeFragment();
    private Fragment frCustomValueInput = new CustomValueInputFragment();



    private HashMap<Integer, Fragment> fragments = new HashMap<>();

    private GameRound gameRoundResult;
    private static Game activeGame;

    public AddNewRoundDialogFragment() {

        ((ChooseGameModeFragment) frChooseGameMode).addRoundDialogListener(this);
        ((ChooseWinnerFragment) frChooseWinner).setRoundDialogListener(this);
        ((ChooseScoreValuesFragment) frChooseScoreValues).setAddNewRoundDialogListener(this);
        ((CustomValueInputFragment) frCustomValueInput).setAddNewRoundDialogListener(this);

        fragments.put(PHASE_CHOOSE_GAME_MODE, frChooseGameMode);
        fragments.put(PHASE_CHOOSE_WINNER, frChooseWinner);
        fragments.put(PHASE_CHOOSE_SCORE_VARIABLES, frChooseScoreValues);
        fragments.put(PHASE_CUSTOM_INPUT, frCustomValueInput);

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

        btnPositive = (SchafkopfButton) view.findViewById(R.id.btn_next);
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

        if(phase == PHASE_CUSTOM_INPUT){
            phase = PHASE_CHOOSE_GAME_MODE;
            changePhase(phase, FragmentController.TRANSITION_IN_LEFT_TO_RIGHT);
            return;
        }

        phase--;
        changePhase(phase, FragmentController.TRANSITION_IN_LEFT_TO_RIGHT);
    }

    private void nextPhase() {

        switch (phase){
            case PHASE_CHOOSE_SCORE_VARIABLES:
                finishAndCloseDialog();
                break;
            case PHASE_CUSTOM_INPUT:
                finishAndCloseDialog();
                break;
            case PHASE_CHOOSE_GAME_MODE:
                String selectedGameMode = gameRoundResult.getGameMode().getName();
                String customGameMode = GameController.ID_GAME_MODE_CUSTOM;

                if(selectedGameMode.equals(customGameMode)){
                    showCustomInputFragment();
                }else{
                    showNextFragment();
                }

                break;
            default:
                showNextFragment();
                break;
        }
    }

    private void showNextFragment() {
        phase++;
        changePhase(phase, FragmentController.TRANSITION_IN_RIGHT_TO_LEFT);
    }

    private void showCustomInputFragment() {
        phase = PHASE_CUSTOM_INPUT;
        changePhase(phase, FragmentController.TRANSITION_IN_RIGHT_TO_LEFT);
    }

    private void finishAndCloseDialog() {
        GameController.getInstance().addGameRoundToActiveGame(gameRoundResult);
        getDialog().dismiss();
        phase = PHASE_CHOOSE_GAME_MODE;
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

            case PHASE_CUSTOM_INPUT:
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

        Fragment nextFragment = fragments.get(phase);

        Bundle args = new Bundle();
        args.putSerializable("roundResult", gameRoundResult);
        nextFragment.setArguments(args);

        transaction.replace(R.id.content_dialog_addRound, nextFragment).commit();
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
    public void onJungfrauChanged(boolean jungfrau) {
        gameRoundResult.setJungfrau(jungfrau);
    }

    @Override
    public void onRoundResultChanged(ChooseScoreValuesFragment.RoundResult result) {

        gameRoundResult.setSchneider(false);
        gameRoundResult.setSchwarz(false);

        switch (result){
            case NONE:
                gameRoundResult.setSchwarz(false);
                gameRoundResult.setSchneider(false);
                break;

            case SCHNEIDER:
                gameRoundResult.setSchneider(true);
            break;

            case SCHWARZ:
                gameRoundResult.setSchwarz(true);
                break;

        }
    }
}
