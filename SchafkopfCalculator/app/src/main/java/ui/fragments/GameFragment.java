package ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.schenk.matthias.schafkopfcalculator.R;

import java.util.ArrayList;

import game.Game;
import game.GameController;
import game.GameRound;
import game.GameSettings;
import ui.AppColors;
import ui.custom.controls.DoubleUpButton;
import ui.custom.controls.fw.SchafkopfButton;
import ui.custom.SchafkopfFragment;
import ui.fragments.dialog.AddNewRoundDialogFragment;
import ui.interfaces.IDoubleUpListener;
import ui.interfaces.IGameFragmentListener;
import ui.interfaces.IGameListener;
import ui.ScoreListAdapter;
import ui.UiUtils;


/**
 * Created by Matthias on 11.01.2018.
 */

public class GameFragment extends SchafkopfFragment implements IGameListener, IDoubleUpListener {

    private TextView lblScorePlayer1, lblScorePlayer2, lblScorePlayer3, lblScorePlayer4, lblPlayer1, lblPlayer2, lblPlayer3, lblPlayer4;
    private SchafkopfButton btnAddRound;
    private DoubleUpButton btnDoubleUp;
    private ListView lstViewRounds;
    private boolean uiCreated = false;
    private ArrayList<IGameFragmentListener> lstGameFragmentListeners = new ArrayList<>();
    private ArrayList<TextView> lstLblsPlayers = new ArrayList<>(), lstLblsPlayerScores = new ArrayList<>();
    private ArrayList<View> lstColorIndicators = new ArrayList<>();
    private Game game;
    private int roundMultiplicator = 1;
    private ScoreListAdapter adapter;
    private LinearLayout layPlayers, layScore;
    private View colorIndicator1, colorIndicator2, colorIndicator3, colorIndicator4;

    public GameFragment() {
        game = new Game(new GameSettings());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findControls(view);
        listeners();
        init();
    }

    private void init() {
        layScore.setBackgroundColor(AppColors.COLOR_PRIMARY_TRANSPARENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiCreated = true;
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiCreated = false;
    }

    private void listeners() {
        GameController.getInstance().addGameListener(this);

        btnAddRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSchafkopfActivity().resetInactivityTimer();
                showAddRoundDialog();
            }
        });

        btnDoubleUp.addDoubleUpListener(this);

        //#OLD
        /*
        btnDoubleUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSchafkopfActivity().vibrate(50);

                doubleUp();
                int doubleUps = UiUtils.multiplicatorToDoubleUps(roundMultiplicator);

                if(doubleUps == 0){
                    btnDoubleUp.setText("Doppeln");
                }else{
                    btnDoubleUp.setText("Gedoppelt: " + doubleUps + " mal");
                }
            }
        });
        */
    }

    //#OLD
    /*
    private void doubleUp() {
        if (roundMultiplicator < game.getSettings().getMaxRoundMultiplicator()) {
            roundMultiplicator *= 2;
        } else {
            roundMultiplicator = 1;
        }
    }
*/

    private void showAddRoundDialog() {
        roundMultiplicator = (int) Math.pow(2, btnDoubleUp.getDoubleUps());


        DialogFragment newFragment = new AddNewRoundDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("multiplicator", roundMultiplicator);
        newFragment.setArguments(bundle);

        newFragment.show(getFragmentManager(), "newRound");
    }

    private void updateUI() {
        if (!uiCreated) return;


        for (int i = 0; i < game.getLstPlayers().size(); i++) {
            //Name
            String playerName = game.getLstPlayers().get(i).getName();
            if (!playerName.equals("")) lstLblsPlayers.get(i).setText(playerName);

            lstColorIndicators.get(i).setBackgroundColor(AppColors.PLAYER_COLORS[i]);
        }

        updateListView(game);
        updateTotalScore(game);

        roundMultiplicator = 1;

        int maxMultiplicator = game.getSettings().getMaxRoundMultiplicator();
        btnDoubleUp.setMaxDoubleUps(UiUtils.multiplicatorToDoubleUps(maxMultiplicator));
        btnDoubleUp.setDoubleUps(game.getCurrentDoubleUps());
    }

    private void onRoundAdded(GameRound round) {
        for (IGameFragmentListener listener : lstGameFragmentListeners) {
            listener.onRoundAdded(round);
        }
    }

    public void addGameSettingsListener(IGameFragmentListener listener) {
        lstGameFragmentListeners.add(listener);
    }

    public void update(Game game) {
        this.game = game;
        updateUI();
    }

    private void findControls(View view) {

        lstLblsPlayers.clear();

        lblPlayer1 = (TextView) view.findViewById(R.id.lbl_player_1);
        lstLblsPlayers.add(lblPlayer1);
        lblPlayer2 = (TextView) view.findViewById(R.id.lbl_player_2);
        lstLblsPlayers.add(lblPlayer2);
        lblPlayer3 = (TextView) view.findViewById(R.id.lbl_player_3);
        lstLblsPlayers.add(lblPlayer3);
        lblPlayer4 = (TextView) view.findViewById(R.id.lbl_player_4);
        lstLblsPlayers.add(lblPlayer4);

        lstColorIndicators.clear();
        colorIndicator1 =  view.findViewById(R.id.color_inidcator_player_1);
        lstColorIndicators.add(colorIndicator1);
        colorIndicator2 = view.findViewById(R.id.color_inidcator_player_2);
        lstColorIndicators.add(colorIndicator2);
        colorIndicator3 = view.findViewById(R.id.color_inidcator_player_3);
        lstColorIndicators.add(colorIndicator3);
        colorIndicator4 =  view.findViewById(R.id.color_inidcator_player_4);
        lstColorIndicators.add(colorIndicator4);

        lstLblsPlayerScores.clear();

        lblScorePlayer1 = (TextView) view.findViewById(R.id.lbl_score_player_1);
        lstLblsPlayerScores.add(lblScorePlayer1);
        lblScorePlayer2 = (TextView) view.findViewById(R.id.lbl_score_player_2);
        lstLblsPlayerScores.add(lblScorePlayer2);
        lblScorePlayer3 = (TextView) view.findViewById(R.id.lbl_score_player_3);
        lstLblsPlayerScores.add(lblScorePlayer3);
        lblScorePlayer4 = (TextView) view.findViewById(R.id.lbl_score_player_4);
        lstLblsPlayerScores.add(lblScorePlayer4);

        btnDoubleUp = (DoubleUpButton) view.findViewById(R.id.btn_double);
        btnAddRound = (SchafkopfButton) view.findViewById(R.id.btn_add_round);

        lstViewRounds = (ListView) view.findViewById(R.id.lst_rounds);

        layPlayers = (LinearLayout) view.findViewById(R.id.lay_players_top);
        //layPlayers.setBackgroundColor(AppColors.COLOR_PRIMARY_TRANSPARENT);
        layScore = (LinearLayout) view.findViewById(R.id.lay_players_score);
    }

    @Override
    public void onGameCreated(Game game, boolean newGame, boolean b, String loadingMessage) {

    }

    private void updateListView(Game game) {
        this.game = game;
        adapter = new ScoreListAdapter(game.getLstRounds(), getActivity().getApplicationContext(), getFragmentManager());
        lstViewRounds.setAdapter(adapter);
        lstViewRounds.setSelection(game.getLstRounds().size() - 1);
    }

    @Override
    public void onGameRoundsChanged(Game game) {
        this.game = game;
        updateUI();
        clearDoubleUps();
    }

    private void clearDoubleUps() {
        btnDoubleUp.clearDoubleUps();
        GameController.getActiveGame().setCurrentDoubleUps(0);

    }

    @Override
    public void onDoubleUp(int doubleUps) {
        GameController.getActiveGame().setCurrentDoubleUps(doubleUps);
    }

    private void updateTotalScore(Game game) {
        for (int i = 0; i < lstLblsPlayerScores.size(); i++) {
            TextView lbl = lstLblsPlayerScores.get(i);
            double score = game.getLstPlayers().get(i).getScore();

            score = UiUtils.formatScoreValue((int) score);

            lbl.setText(UiUtils.getScoreString(score));
            lbl.setTextColor(AppColors.getScoreTextColor(score, false, false, true));
        }
    }
}
