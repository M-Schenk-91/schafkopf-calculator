package game;

import java.io.Serializable;
import java.util.ArrayList;

import ui.UiUtils;

/**
 * Created by Matthias on 13.01.2018.
 */

public class GameRound implements Serializable {

    private int numPlayers;
    private Game game = null;
    private GameMode gameMode;
    private boolean[] lstWinners = null;
    private boolean schneider = false, schwarz = false;
    private int multiplicator = 1;
    private int[] lstScoreChangesPerPlayer;
    private int lauf;
    private boolean jungfrau = false;

    public GameRound(){
        gameMode = GameController.getInstance().getHmAvailableModes().get(GameController.ID_GAME_MODE_DEFAULT);
    }

    public GameRound(Game game) {
        this();
        this.numPlayers = game.getLstPlayers().size();
        this.game = game;
        lstScoreChangesPerPlayer = new int[numPlayers];
    }

    public GameRound(int numPlayers) {
        this();
        this.numPlayers = numPlayers;
        lstScoreChangesPerPlayer = new int[numPlayers];
    }

    @Override
    public String toString() {
        String result = "";
        String separator = " - ";


        result += getGameMode().getName();

        if(getMultiplicator() > 1){
            result += separator;
            result += UiUtils.multiplicatorToDoubleUps(getMultiplicator()) + " mal gedoppelt";
        }

        if(jungfrau){
            result += separator;
            result += "Jungfrau";
        }

        if(getLauf() > 0){
            result += separator;
            result += getLauf() + " Lauf";
        }

        if(gameMode.getName().equals(GameController.ID_GAME_MODE_RAMSCH)) return result;

        if(isSchneider()){
            result += separator;
            result += "Schneider";
        }

        if(isSchwarz()){
            result += separator;
            result += "Schwarz";
        }

        if(!isSchneider() && !isSchwarz() && !gameMode.getName().equals(GameController.ID_GAME_MODE_CUSTOM)){
            result += separator;
            result += "Schneiderfrei";
        }

        return result;
    }

    public int getLauf() {
        return lauf;
    }

    public void setLauf(int lauf) {
        this.lauf = lauf;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setSchneider(boolean schneider) {
        this.schneider = schneider;
    }

    public void setSchwarz(boolean schwarz) {
        this.schwarz = schwarz;
    }

    public void setMultiplicator(int multiplicator) {
        this.multiplicator = multiplicator;
    }

    public void setLstWinners(boolean[] lstWinners) {
        this.lstWinners = lstWinners;
    }

    public boolean[] getLstWinners() {
        return lstWinners;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getRoundScoreChangeForPlayer(int player) {
        return lstScoreChangesPerPlayer[player];
    }

    public void calculate() {

        if(gameMode.getName().equals(GameController.ID_GAME_MODE_CUSTOM)) return;

        for (int i = 0; i < numPlayers; i++) {
            int change = calculatePlayerScoreChange(i);

            lstScoreChangesPerPlayer[i] = multiplyChangePerPlayerDependingOnGameType(change, i);
        }
    }

    private int multiplyChangePerPlayerDependingOnGameType(int change, int i) {

        int numWinners = getNumWinners(lstWinners);

        if (lstWinners[i]) {
            if (numWinners == 2 || numWinners == 3) {
                return change;
            } else if (numWinners == 1) {
                return 3 * change;
            }
        } else {
            if (numWinners == 3) {
                return -3 * change;
            } else if (numWinners == 1 || numWinners == 2) {
                return -change;
            }
        }

        return -1;
    }

    private int calculatePlayerScoreChange(int i) {
        int result = 0;
        result += gameMode.getValue();

        if (schwarz) {
            result += game.getSettings().getValueSchwarz() + game.getSettings().getValueSchneider();
        } else if (schneider) {
            result += game.getSettings().getValueSchneider();
        }

        if (lauf > 0) {
            for (int j = 0; j < lauf; j++) {
                result += game.getSettings().getValueLauf();
            }
        }

        int multiply = multiplicator;
        if(jungfrau) multiply *= 2;

        result *= multiply;

        return result;
    }

    public static int getNumWinners(boolean[] winners) {
        return getWinnerPlayerNums(winners).size();
    }

    private static ArrayList<Integer> getWinnerPlayerNums(boolean[] winners) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < winners.length; i++) {
            if (winners[i]) ids.add(i);
        }
        return ids;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean isSchneider() {
        return schneider;
    }

    public boolean isSchwarz() {
        return schwarz;
    }

    public int getMultiplicator() {
        return multiplicator;
    }

    public void setValueChanges(int[] valueChanges) {
        this.lstScoreChangesPerPlayer = valueChanges;
    }

    public void setJungfrau(boolean jungfrau) {
        this.jungfrau = jungfrau;
    }

    public void setLstScoreChangesPerPlayer(int[] lstScoreChangesPerPlayer) {
        this.lstScoreChangesPerPlayer = lstScoreChangesPerPlayer;
    }
}
