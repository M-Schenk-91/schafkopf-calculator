package game;

import java.util.ArrayList;

/**
 * Created by Matthias on 11.01.2018.
 */

public class GameSettings {

    public static final int DEFAULT_COUNT_MAX_PLAYERS = 4;
    public static final int DEFAULT_MAX_ROUND_MULTIPLICATOR = 1024;

    private int numPlayers = DEFAULT_COUNT_MAX_PLAYERS;
    private int maxRoundMultiplicator = DEFAULT_MAX_ROUND_MULTIPLICATOR;

    private String[] lstPlayerNames;
    private ArrayList<GameMode> lstModes = new ArrayList<>();
    private int valueSchneider, valueSchwarz, valueLauf;

    public GameSettings(int valueSchneider, int valueSchwarz, int valueLauf) {
        this.valueSchneider = valueSchneider;
        this.valueSchwarz = valueSchwarz;
        this.valueLauf = valueLauf;

        lstPlayerNames = new String[]{"Spieler 1", "Spieler 2", "Spieler 3", "Spieler 4"};
    }

    public GameSettings() {
        this(5, 5, 5);
    }

    public void setLstPlayerNames(String[] names) {
        lstPlayerNames = names;
    }

    public String[] getLstPlayerNames() {
        return lstPlayerNames;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<GameMode> getLstModes() {
        return lstModes;
    }

    public GameMode getGameMode(String name) {
        for (GameMode mode : lstModes) {
            if (mode.getName().equals(name)) return mode;
        }

        return null;
    }

    public void addGameMode(GameMode mode) {
        lstModes.add(mode);
    }

    public void setGameModes(ArrayList<GameMode> gameModes) {
        this.lstModes = gameModes;
    }

    public int getValueSchneider() {
        return valueSchneider;
    }

    public int getValueSchwarz() {
        return valueSchwarz;
    }

    public int getValueLauf() {
        return valueLauf;
    }

    public int getMaxRoundMultiplicator() {
        return maxRoundMultiplicator;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setMaxRoundMultiplicator(int maxMultiplicator) {
        this.maxRoundMultiplicator = maxMultiplicator;
    }
}
