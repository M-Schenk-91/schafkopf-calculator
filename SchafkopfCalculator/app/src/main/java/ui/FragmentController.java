package ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.schenk.matthias.schafkopfcalculator.R;

import java.util.HashMap;

import game.Game;
import ui.custom.SchafkopfFragment;
import ui.fragments.GameFragment;
import ui.fragments.GameSetupFragment;
import ui.fragments.StatisticsFragment;

/**
 * Created by Matthias on 11.01.2018.
 */

public class FragmentController {

    public static final int TRANSITION_NONE = -1;
    public static final int TRANSITION_IN_LEFT_TO_RIGHT = R.anim.enter_from_left;
    public static final int TRANSITION_IN_RIGHT_TO_LEFT = R.anim.enter_from_right;

    public static final int FRAGMENT_GAME_SETUP = 0;
    public static final int FRAGMENT_GAME = 1;
    public static final int FRAGMENT_STATISTICS = 2;

    private int currentFragment = 0;
    private FragmentManager mgrFragments;
    private HashMap<Integer, SchafkopfFragment> fragments = new HashMap<>();

    private GameSetupFragment frGameSetup = new GameSetupFragment();
    private GameFragment frGame = new GameFragment();
    private StatisticsFragment frStatistics = new StatisticsFragment();


    private FragmentActivity activity;

    public FragmentController(Context context) {
        this.activity = (FragmentActivity) context;
        this.mgrFragments = activity.getSupportFragmentManager();

        fragments.put(FRAGMENT_GAME_SETUP, frGameSetup);
        fragments.put(FRAGMENT_GAME, frGame);
        fragments.put(FRAGMENT_STATISTICS, frStatistics);
    }

    public void setFragment(int fragment, boolean animation) {
        FragmentTransaction transaction = mgrFragments.beginTransaction();

        if(animation){
            int animationExit = -1;
            int animationIntro = -1;

            if (fragment > currentFragment){
                animationExit = R.anim.exit_to_left;
                animationIntro = R.anim.enter_from_right;
            }

            if (fragment < currentFragment){
                animationExit = R.anim.exit_to_right;
                animationIntro = R.anim.enter_from_left;
            }

            transaction.setCustomAnimations(animationIntro, animationExit);
        }

        closeKeyboardIfOpen();

        if (!activity.isFinishing()){
            transaction.replace(R.id.content, getFragment(fragment)).commit();
            currentFragment = fragment;
        }
    }

    private void closeKeyboardIfOpen() {
        View focus = (activity.getCurrentFocus());
        if (focus != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    public SchafkopfFragment getFragment(int fragment) {
        return fragments.get(fragment);
    }

    public void updateFragments(Game game){
        for (SchafkopfFragment fragment: fragments.values()) {
            fragment.update(game);
        }
    }
}
