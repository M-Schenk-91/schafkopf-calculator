package ui.fragments.dialog;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schenk.matthias.schafkopfcalculator.R;

import java.util.ArrayList;

import game.Game;
import game.GameController;
import game.GameRound;
import ui.AppColors;
import ui.custom.controls.fw.SchafkopfToggleButton;
import ui.interfaces.IRoundDialogListener;

public class ChooseWinnerFragment extends Fragment {

    private IRoundDialogListener listener;
    private ArrayList<SchafkopfToggleButton> lstTogglesWinner = new ArrayList<>();
    private boolean[] winners = null;
    private int proceedCondition = 2;
    private Game activeGame;

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

            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 16 );
            shape.setColor(AppColors.PLAYER_COLORS[i]);

            lstTogglesWinner.get(i).setBackground(shape);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activeGame = GameController.getInstance().getActiveGame();

        int numPlayers = activeGame.getLstPlayers().size();
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
                    listener.onPhaseCompleted(AddNewRoundDialogFragment.PHASE_CHOOSE_WINNER, proceed);
                }
            });
        }

        updateToggleButtonTexts();
    }
}
