package game;

import java.io.Serializable;

/**
 * Created by Matthias on 13.01.2018.
 */

public class Player implements Serializable {

    private int num;
    private String name = "Spieler";
    private int score = 0;
    private boolean dealer = false;

    public Player(int num) {
        this.name = "Spieler " + num;
        this.num = num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setDealer(boolean dealer) {
        this.dealer = dealer;
    }

    public int getNum() {
        return num;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public boolean isDealer() {
        return dealer;
    }
}
