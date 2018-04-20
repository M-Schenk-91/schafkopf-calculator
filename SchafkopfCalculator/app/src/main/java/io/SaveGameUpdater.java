package io;

import java.util.ArrayList;
import java.util.HashMap;

import game.Game;
import game.GameController;
import game.GameMode;

/**
 * Created by Matthias on 20.04.2018.
 */

public class SaveGameUpdater {

    private static final String ID_GAME_MODE_SOLO_OLD = "Solo/Wenz";

    private static SaveGameUpdater instance;
    private ArrayList<GameMode> modes = new ArrayList<>();

    public SaveGameUpdater(){}

    public static SaveGameUpdater getInstance(){
        if(instance == null) instance = new SaveGameUpdater();
        return instance;
    }

    public void update(Game game) throws DataException {
        updateGameModes(game);
    }

    private void updateGameModes(Game game) {
        addNewModes(game);
        correctModeNames(game);
    }

    private void correctModeNames(Game game) {
        for (GameMode mode: game.getSettings().getLstModes()) {
            if(mode.getName().equals(ID_GAME_MODE_SOLO_OLD)) mode.setName(GameController.ID_GAME_MODE_SOLO);
        }
    }

    private void addNewModes(Game game) {
        for (GameMode mode: modes) {
            boolean alreadyExists = false;
            String newModeID = mode.getName();
            ArrayList<GameMode> modesInLoadedGame = game.getSettings().getLstModes();

            for (int i = 0; i < modesInLoadedGame.size(); i++) {
                if (modesInLoadedGame.get(i).getName().equals(newModeID)) alreadyExists = true;
            }

            if (!alreadyExists) game.getSettings().addGameMode(mode);
        }
    }

    public void setGameModeUpdate(HashMap<String, GameMode> hmAvailableModes) {
        modes = new ArrayList<>(hmAvailableModes.values());
    }
}

