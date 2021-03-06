package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ui.interfaces.IGameListener;

/**
 * Created by Matthias on 11.01.2018.
 */

public class GameController {

    private static GameController instance;

    public static final String ID_GAME_MODE_DEFAULT = "Sauspiel";
    public static final String ID_GAME_MODE_SOLO = "Solo";
    public static final String ID_GAME_MODE_WENZ = "Wenz";
    public static final String ID_GAME_MODE_RAMSCH = "Ramsch";
    public static final String ID_GAME_MODE_GEIER = "Geier";
    public static final String ID_GAME_MODE_CUSTOM = "Manuelle Eingabe";
    public static final String ID_GAME_MODE_WEDDING = "Hochzeit";
    public static final String ID_GAME_MODE_COLORED_WENZ = "Farbwenz";
    public static final String ID_GAME_MODE_COLORED_GEIER = "Farbgeier";
    public static final String ID_GAME_MODE_BETTEL = "Bettel";


    private LinkedHashMap<String, GameMode> hmAvailableModes = new LinkedHashMap<>();
    private ArrayList<IGameListener> listeners = new ArrayList<>();
    private static Game activeGame;

    private GameController() {
        initGameModes();
    }

    public void setActiveGame(Game activeGame) {
        GameController.activeGame = activeGame;
    }

    private void initGameModes() {
        hmAvailableModes.put(ID_GAME_MODE_DEFAULT, new GameMode(ID_GAME_MODE_DEFAULT, 10, false, true));
        hmAvailableModes.put(ID_GAME_MODE_WEDDING, new GameMode(ID_GAME_MODE_WEDDING, 10, false, false));
        hmAvailableModes.put(ID_GAME_MODE_SOLO, new GameMode(ID_GAME_MODE_SOLO, 20, true, true ));
        hmAvailableModes.put(ID_GAME_MODE_WENZ, new GameMode(ID_GAME_MODE_WENZ, 20, true, true));
        hmAvailableModes.put(ID_GAME_MODE_COLORED_WENZ, new GameMode(ID_GAME_MODE_COLORED_WENZ, 20, true, false));
        hmAvailableModes.put(ID_GAME_MODE_GEIER, new GameMode(ID_GAME_MODE_GEIER, 20, true, false));
        hmAvailableModes.put(ID_GAME_MODE_COLORED_GEIER, new GameMode(ID_GAME_MODE_COLORED_GEIER, 20, true, false));
        hmAvailableModes.put(ID_GAME_MODE_RAMSCH, new GameMode(ID_GAME_MODE_RAMSCH, 10, true, false));
        hmAvailableModes.put(ID_GAME_MODE_BETTEL, new GameMode(ID_GAME_MODE_BETTEL, 10, true, false));
        hmAvailableModes.put(ID_GAME_MODE_CUSTOM, new GameMode(ID_GAME_MODE_CUSTOM, 10, false, true));
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
            listener.onGameCreated(game, newGame, false, "");
        }
    }

    public LinkedHashMap<String, GameMode> getHmAvailableModes() {
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
