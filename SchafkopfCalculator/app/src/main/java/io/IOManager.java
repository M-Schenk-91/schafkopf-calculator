package io;

import android.content.Context;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import game.Game;
import game.GameRound;
import game.GameSettings;
import ui.interfaces.IGameListener;

/**
 * Created by Matthias on 09.02.2018.
 */

public class IOManager {

    private static String FILENAME_GAME_SETTINGS = "GameSettings.txt";
    private static String FILENAME_GAME_STATUS = "GameStatus.txt";
    private static String FILENAME_GAME_ROUNDS = "GameRounds.txt";

    public static String NAME_DIRECTORY_CURRENTLY_CACHED_GAME = "cached";

    private final Context context;
    private ArrayList<IGameListener> listeners = new ArrayList<>();

    public IOManager(Context context) {
        this.context = context;
    }

    public void addGameListener(IGameListener listener) {
        listeners.add(listener);
    }

    public ArrayList<String> getSavedGameNames() {
        ArrayList<String> savedGames = new ArrayList<>();

        File internalStorage = context.getFilesDir();

        for (File file : internalStorage.listFiles()) {

            if (!file.isDirectory()) continue;
            if (file.getName().equals(NAME_DIRECTORY_CURRENTLY_CACHED_GAME)) continue;

            savedGames.add(file.getName());
        }

        return savedGames;
    }

    public boolean savedGamesExisting() {
        return getSavedGameNames().size() > 0;
    }

    public void saveGame(Game game, String dirName) {

        File internalStorage = context.getFilesDir();
        FileOutputStream fos = null;

        try {

            String pathGameDir = internalStorage.getCanonicalPath() + File.separator + dirName;

            String pathSettings = pathGameDir + File.separator + FILENAME_GAME_SETTINGS;
            String pathStatus = pathGameDir + File.separator + FILENAME_GAME_STATUS;
            String pathRounds = pathGameDir + File.separator + FILENAME_GAME_ROUNDS;

            File fileSettings = prepareFile(pathSettings);
            fos = new FileOutputStream(fileSettings);
            IOUtils.writeToFile(fos, JSONManager.settingsToJSON(game.getSettings()));

            File fileStatus = prepareFile(pathStatus);
            fos = new FileOutputStream(fileStatus);
            //to be expanded
            IOUtils.writeToFile(fos, JSONManager.statusToJSON(game.getCurrentDoubleUps()));

            File fileData = prepareFile(pathRounds);
            fos = new FileOutputStream(fileData);
            IOUtils.writeToFile(fos, JSONManager.roundsToJSON(game.getLstRounds()));

            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File prepareFile(String filePath) throws IOException {
        File fileSettings = new File(filePath);
        if (fileSettings.exists()) fileSettings.delete();

        fileSettings.getParentFile().mkdirs();
        fileSettings.createNewFile();
        return fileSettings;
    }

    public Game loadGame(String dirName, boolean updateSavedGame) {
        Game result = null;

        File internalStorage = context.getFilesDir();
        FileInputStream fis = null;

        try {

            String pathGameDir = internalStorage.getCanonicalPath() + File.separator + dirName;
            String pathSettings = pathGameDir + File.separator + FILENAME_GAME_SETTINGS;
            String pathStatus = pathGameDir + File.separator + FILENAME_GAME_STATUS;
            String pathData = pathGameDir + File.separator + FILENAME_GAME_ROUNDS;

            File fileSettings = new File(pathSettings);

            if (!fileSettings.exists()) {
                return null;
            }

            fis = new FileInputStream(fileSettings);
            GameSettings settings = JSONManager.JSONToSettings(fis);

            if (settings == null) {
                return null;
            }

            result = new Game(settings);

            File fileStatus = new File(pathStatus);
            if(fileStatus.exists()){
                fis = new FileInputStream(fileStatus);
                result.setCurrentDoubleUps(JSONManager.JSONToGameStatus(fis));
            }

            File fileDataRounds = new File(pathData);

            if (!fileDataRounds.exists()) {
                return null;
            }

            fis = new FileInputStream(fileDataRounds);
            ArrayList<GameRound> gameRounds = JSONManager.JSONToRoundData(fis);

            if (gameRounds == null) {
                return null;
            }

            result.addRounds(gameRounds);

            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean cached = dirName.equals(NAME_DIRECTORY_CURRENTLY_CACHED_GAME);

        String loadingMessage = "";

        if(updateSavedGame){
            loadingMessage = updateSavedGame(result);
        }

        onGameLoaded(result, cached, loadingMessage);
        return result;
    }

    private String updateSavedGame(Game result) {
        SaveGameUpdater updater = SaveGameUpdater.getInstance();

        try {
            updater.update(result);
        } catch (DataException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "";
    }

    private void onGameLoaded(Game result, boolean cached, String loadingMessage) {
        for (IGameListener l : listeners) {
            l.onGameCreated(result, false, cached, loadingMessage);
        }
    }
}
