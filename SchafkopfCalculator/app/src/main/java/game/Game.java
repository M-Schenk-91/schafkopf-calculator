package game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Matthias on 11.01.2018.
 */

public class Game implements Serializable {

    private ArrayList<Player> lstPlayers = new ArrayList<>();
    private ArrayList<GameRound> lstRounds = new ArrayList<>();
    private GameSettings settigs;

    public Game(GameSettings settings) {
        this.settigs = settings;
        initPlayers();
    }

    private void initPlayers() {
        lstPlayers.clear();

        for (int i = 0; i < settigs.getNumPlayers(); i++) {
            Player player = new Player(i);
            player.setName(settigs.getLstPlayerNames()[i]);
            player.setScore(0);
            player.setDealer(i == 0);

            addPlayer(player);
        }
    }

    public void addPlayer(Player player) {
        lstPlayers.add(player);
    }

    public void setSettigs(GameSettings settigs) {
        this.settigs = settigs;
    }

    public GameSettings getSettings() {
        return settigs;
    }

    public ArrayList<Player> getLstPlayers() {
        return lstPlayers;
    }

    public void addRound(GameRound gameRound) {
        lstRounds.add(gameRound);
        calculateScore();
    }

    private void calculateScore() {
        for (int i = 0; i < lstPlayers.size(); i++) {
            lstPlayers.get(i).setScore(0);
            int score = 0;
            for (int j = 0; j < lstRounds.size(); j++) {
                score += lstRounds.get(j).getRoundScoreChangeForPlayer(i);
            }
            lstPlayers.get(i).setScore(score);
        }
    }

    public ArrayList<GameRound> getLstRounds() {
        return lstRounds;
    }

    public void removeRound(int position) {
        lstRounds.remove(position);
        calculateScore();
    }

    public void addRounds(ArrayList<GameRound> gameRounds) {
        lstRounds.addAll(gameRounds);
        calculateScore();
    }
}
