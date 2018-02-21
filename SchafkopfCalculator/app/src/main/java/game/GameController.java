package game;

import java.util.ArrayList;
import java.util.HashMap;

import ui.interfaces.IGameListener;

/**
 * Created by Matthias on 11.01.2018.
 */

public class GameController {

    private static GameController instance;

    public static final String ID_GAME_MODE_DEFAULT = "Sauspiel";
    public static final String ID_GAME_MODE_SOLO = "Solo/Wenz";

    private HashMap<String, GameMode> hmAvailableModes = new HashMap<>();
    private ArrayList<IGameListener> listeners = new ArrayList<>();
    private static Game activeGame;

    private GameController() {
        initGameModes();
    }

    public void setActiveGame(Game activeGame) {
        GameController.activeGame = activeGame;
    }

    private void initGameModes() {
        hmAvailableModes.put(ID_GAME_MODE_DEFAULT, new GameMode(ID_GAME_MODE_DEFAULT, 10, false));
        hmAvailableModes.put(ID_GAME_MODE_SOLO, new GameMode(ID_GAME_MODE_SOLO, 20, true));
    }

    public static GameController getInstance() {
        if (instance == null) instance = new GameController();
        return instance;
    }

    public void createNewGame(GameSettings settings) {
        activeGame = new Game(settings);
        onNewGameCreated(activeGame, true);
    }

    public static Game getActiveGame() {
        return activeGame;
    }

    private void onNewGameCreated(Game game, boolean newGame) {
        for (IGameListener listener : listeners) {
            listener.onGameCreated(game, newGame, false);
        }
    }

    public HashMap<String, GameMode> getHmAvailableModes() {
        return hmAvailableModes;
    }

    public void addGameListener(IGameListener listener) {
        listeners.add(listener);
    }

    public void addGameRoundToActiveGame(GameRound gameRoundResult) {
        gameRoundResult.calculate();
        activeGame.addRound(gameRoundResult);
        onGameRoundsChanged(activeGame);
    }

    private void onGameRoundsChanged(Game game) {
        for (IGameListener li : listeners) {
            li.onGameRoundsChanged(game);
        }
    }

    public void deleteGameRoundOfActiveGame(int position) {
        activeGame.removeRound(position);
        onGameRoundsChanged(activeGame);
    }
}
