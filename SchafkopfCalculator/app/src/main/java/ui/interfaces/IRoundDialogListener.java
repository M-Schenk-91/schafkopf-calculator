package ui.interfaces;

import game.GameMode;
import ui.fragments.dialog.AddNewRoundDialogFragment;

/**
 * Created by Matthias on 26.01.2018.
 */

public interface IRoundDialogListener {
    void onPhaseCompleted(int finishedPhase, boolean proceed);

    void onGameModeChanged(GameMode mode);
    void onWinnersChanged(boolean[] winners);
    void onLaufChanged(int lauf);
    void onRoundResultChanged(AddNewRoundDialogFragment.ChooseScoreValuesFragment.RoundResult result);
}
