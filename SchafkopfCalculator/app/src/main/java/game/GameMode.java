package game;

import java.io.Serializable;

/**
 * Created by Matthias on 13.01.2018.
 */

public class GameMode implements Serializable {

    private String name = "Spielmodus";
    private int value = 0;
    private boolean solo = false;
    private boolean isActive;

    public GameMode(String name, int value, boolean isSolo, boolean isActive) {
        this.name = name;
        this.value = value;
        this.solo = isSolo;
        this.isActive = isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setSolo(boolean solo) {
        this.solo = solo;
    }

    public boolean isSolo() {
        return solo;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
