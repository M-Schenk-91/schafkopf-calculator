package ui.custom;

import android.support.v4.app.Fragment;

import game.Game;

/**
 * Created by Matthias on 20.02.2018.
 */

public class SchafkopfFragment extends Fragment {

    public void update(Game game){}

    public SchafkopfActivity getSchafkopfActivity(){
        return (SchafkopfActivity) getActivity();
    }
}
