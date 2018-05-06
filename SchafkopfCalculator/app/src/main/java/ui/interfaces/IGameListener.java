package ui.interfaces;

import game.Game;

/**
 * Created by Matthias on 25.01.2018.
 */

public interface IGameListener {

    void onGameCreated(Game game, boolean newGame, boolean cachedGame, String loadingMessage);

    void onGameRoundsChanged(Game game);
}
