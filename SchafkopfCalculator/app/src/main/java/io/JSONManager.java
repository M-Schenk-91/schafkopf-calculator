package io;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import game.GameMode;
import game.GameRound;
import game.GameSettings;

/**
 * Created by Matthias on 09.02.2018.
 */

public class JSONManager {

   public static String settingsToJSON(GameSettings settings) throws JSONException {
      String result = "";

      JSONObject objSettings = new JSONObject();

      //GLOBAL
      objSettings.put("numplayers", settings.getNumPlayers());
      objSettings.put("maxroundmultiplicator", settings.getMaxRoundMultiplicator());

      //VALUES
      objSettings.put("schneider", settings.getValueSchneider());
      objSettings.put("schwarz", settings.getValueSchwarz());
      objSettings.put("lauf", settings.getValueLauf());

      //MODES
      JSONArray objGameModes = new JSONArray();
      ArrayList<GameMode> modes = settings.getLstModes();

      for (int i = 0; i < modes.size(); i++) {
         GameMode mode = modes.get(i);
         JSONObject objMode = new JSONObject();
         objMode.put("name", mode.getName());
         objMode.put("value", mode.getValue());
         objMode.put("solo", mode.isSolo());
         objMode.put("active", mode.isActive());

         objGameModes.put(objMode);
      }

      objSettings.put("gamemodes", objGameModes);

      // PLAYER NAMES
      JSONArray objPlayerNames = new JSONArray();
      String[] playerNames = settings.getLstPlayerNames();

      for (int i = 0; i < playerNames.length; i++) {
         JSONObject objPlayerName = new JSONObject();
         objPlayerName.put("playerid", i);
         objPlayerName.put("name", playerNames[i]);

         objPlayerNames.put(objPlayerName);
      }

      objSettings.put("playernames", objPlayerNames);

      //DONE
      result = objSettings.toString();
      return result;
   }

   public static GameSettings JSONToSettings(FileInputStream fis) throws IOException {

      GameSettings result = null;

      JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));

      ArrayList<GameMode> modes = new ArrayList<>();
      String[] playerNames = null;
      int numPlayers = 0;
      int valSchneider = 0;
      int valSchwarz = 0;
      int valLauf = 0;
      int multiplicator = 0;

      reader.beginObject();
      while (reader.hasNext()) {

         String name = reader.nextName();
         if (name.equals("numplayers")) {
            numPlayers = reader.nextInt();
         } else if (name.equals("maxroundmultiplicator")) {
            multiplicator = reader.nextInt();
         } else if (name.equals("schneider")) {
            valSchneider = reader.nextInt();
         } else if (name.equals("schwarz")) {
            valSchwarz = reader.nextInt();
         } else if (name.equals("lauf")) {
            valLauf = reader.nextInt();
         } else if (name.equals("gamemodes")) {
            modes.addAll(readModes(reader));
         } else if (name.equals("playernames")) {
            playerNames = readPlayerNames(reader);
         } else {
            reader.skipValue();
         }
      }

      reader.endObject();

      result = new GameSettings(valSchneider, valSchwarz, valLauf);
      result.setNumPlayers(numPlayers);
      result.setMaxRoundMultiplicator(multiplicator);
      result.setGameModes(modes);
      result.setLstPlayerNames(playerNames);

      return result;
   }

   public static String roundsToJSON(ArrayList<GameRound> rounds) throws JSONException {
      String result = "";

      JSONArray objRounds = new JSONArray();

      for (int i = 0; i < rounds.size(); i++) {

         GameRound round = rounds.get(i);
         JSONObject objRound = new JSONObject();

         //FIRST ELEMENT FOR VALUES
         objRound.put("roundid", i);
         objRound.put("numplayers", round.getNumPlayers());
         objRound.put("schneider", round.isSchneider());
         objRound.put("schwarz", round.isSchwarz());
         objRound.put("lauf", round.getLauf());
         objRound.put("multiplicator", round.getMultiplicator());

         //MODE
         GameMode mode = round.getGameMode();
         JSONObject objMode = new JSONObject();
         objMode.put("name", mode.getName());
         objMode.put("value", mode.getValue());
         objMode.put("solo", mode.isSolo());

         objRound.put("gamemode", objMode);

         //DATA
         JSONArray objWinners = new JSONArray();
         JSONArray objvalueChanges = new JSONArray();

         boolean winners[] = round.getLstWinners();

         for (int j = 0; j < round.getNumPlayers(); j++) {
            objWinners.put(winners[j]);
            objvalueChanges.put(round.getRoundScoreChangeForPlayer(j));
         }

         objRound.put("winners", objWinners);
         objRound.put("values", objvalueChanges);

         objRounds.put(objRound);
      }

      //DONE
      result = objRounds.toString();
      return result;
   }

   public static ArrayList<GameRound> JSONToRoundData(FileInputStream fis) throws IOException {
      ArrayList<GameRound> result = new ArrayList<>();

      JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));

      reader.beginArray();
      while (reader.hasNext()) {
         result.add(readGameRound(reader));
      }

      reader.endArray();
      return result;
   }

   public static String statusToJSON(int currentDoubleUps) throws JSONException {
      JSONObject objSettings = new JSONObject();
      objSettings.put("currentDoubleUps", currentDoubleUps);
      return objSettings.toString();
   }

   public static int JSONToGameStatus(FileInputStream fis) throws IOException {
      int currentDoubleUps = 0;
      JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
      reader.beginObject();
      while (reader.hasNext()) {

         String name = reader.nextName();
         if (name.equals("currentDoubleUps")) {
            currentDoubleUps = reader.nextInt();
         } else {
            reader.skipValue();
         }
      }
      reader.endObject();
      return currentDoubleUps;
   }

   private static String[] readPlayerNames(JsonReader reader) throws IOException {
      ArrayList<String> result = new ArrayList<>();

      reader.beginArray();
      while (reader.hasNext()) {
         result.add(readPlayerName(reader));
      }

      reader.endArray();

      return result.toArray(new String[result.size()]);
   }

   private static String readPlayerName(JsonReader reader) throws IOException {
      int id = 0;
      String playerName = "";

      reader.beginObject();
      while (reader.hasNext()) {
         String name = reader.nextName();

         if (name.equals("playerid")) {
            id = reader.nextInt();
         } else if (name.equals("name")) {
            playerName = reader.nextString();
         } else {
            reader.skipValue();
         }
      }

      reader.endObject();

      return playerName;
   }

   private static Collection<? extends GameMode> readModes(JsonReader reader) throws IOException {
      ArrayList<GameMode> result = new ArrayList<>();

      reader.beginArray();
      while (reader.hasNext()) {
         result.add(readMode(reader));
      }

      reader.endArray();

      return result;
   }

   private static GameMode readMode(JsonReader reader) throws IOException {
      String modeName = "";
      int value = 0;
      boolean solo = false;
      boolean active = true;

      reader.beginObject();
      while (reader.hasNext()) {
         String name = reader.nextName();

         if (name.equals("name")) {
            //JSONObject strangely puts \ in front of every / so we have to cut it out
            modeName = reader.nextString()
                  .replaceAll("\\\\", "");
         } else if (name.equals("value")) {
            value = reader.nextInt();
         } else if (name.equals("solo")) {
            solo = reader.nextBoolean();
         } else if (name.equals("active")) {
            active = reader.nextBoolean();
         } else {
            reader.skipValue();
         }
      }
      reader.endObject();

      return new GameMode(modeName, value, solo, active);
   }

   private static GameRound readGameRound(JsonReader reader) throws IOException {
      GameRound result = null;

      int id = 0;
      int numPlayers = 0;
      boolean schneider = false;
      boolean schwarz = false;
      int lauf = 0;
      int multiplicator = 0;
      boolean[] winners = null;
      int[] valueChanges = null;
      GameMode mode = null;

      reader.beginObject();
      while (reader.hasNext()) {

         String name = reader.nextName();
         if (name.equals("roundid")) {
            id = reader.nextInt();
         } else if (name.equals("numplayers")) {
            numPlayers = reader.nextInt();
         } else if (name.equals("schneider")) {
            schneider = reader.nextBoolean();
         } else if (name.equals("schwarz")) {
            schwarz = reader.nextBoolean();
         } else if (name.equals("lauf")) {
            lauf = reader.nextInt();
         } else if (name.equals("multiplicator")) {
            multiplicator = reader.nextInt();
         } else if (name.equals("winners")) {
            winners = readWinners(reader);
         } else if (name.equals("values")) {
            valueChanges = readValueChanges(reader);
         } else if (name.equals("gamemode")) {
            mode = readMode(reader);
         } else {
            reader.skipValue();
         }
      }

      reader.endObject();

      result = new GameRound(numPlayers);
      result.setSchneider(schneider);
      result.setSchwarz(schwarz);
      result.setLauf(lauf);
      result.setMultiplicator(multiplicator);
      result.setLstWinners(winners);
      result.setValueChanges(valueChanges);
      result.setGameMode(mode);

      return result;
   }

   private static int[] readValueChanges(JsonReader reader) throws IOException {
      ArrayList<Integer> resultTemp = new ArrayList<>();

      reader.beginArray();
      while (reader.hasNext()) {
         resultTemp.add(reader.nextInt());
      }
      reader.endArray();

      //ArrayList to Array
      int[] result = new int[resultTemp.size()];
      for (int i = 0; i < result.length; i++) {
         result[i] = resultTemp.get(i);
      }

      return result;
   }

   private static boolean[] readWinners(JsonReader reader) throws IOException {
      ArrayList<Boolean> resultTemp = new ArrayList<>();

      reader.beginArray();
      while (reader.hasNext()) {
         resultTemp.add(reader.nextBoolean());
      }
      reader.endArray();

      //ArrayList to Array
      boolean[] result = new boolean[resultTemp.size()];
      for (int i = 0; i < result.length; i++) {
         result[i] = resultTemp.get(i);
      }

      return result;
   }
}
