package ui.interfaces;

import game.GameMode;
import game.GameRound;
import ui.fragments.dialog.ChooseScoreValuesFragment;

/**
 * Created by Matthias on 26.01.2018.
 */

public interface IRoundDialogListener {
    void onPhaseCompleted(int finishedPhase, boolean proceed);
    void onGameModeChanged(GameMode mode);
    void onWinnersChanged(boolean[] winners);
    void onLaufChanged(int lauf);
    void onJungfrauChanged(boolean jungfrau);
    void onRoundResultChanged(ChooseScoreValuesFragment.RoundResult result);
    void onProceedAllowedChanged(boolean proceedAllowed);
    void onCustomRoundChanged(GameRound gameRoundResult);
}
