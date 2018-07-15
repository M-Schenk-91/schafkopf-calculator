package game

import ui.interfaces.IGameListener
import ui.interfaces.IStatisticsListener
import java.io.Serializable

object Statistics : Serializable, IGameListener {

   private var listeners: ArrayList<IStatisticsListener> = ArrayList()
   lateinit var game: Game

   var roundsPlayed: Int = 0

   var normalGames: Int = 0;
   var normalGamesPercent: Int = 0;

   var solos: Int = 0;
   var solosPercent: Int = 0;

   var wenzen: Int = 0;
   var wenzenPercent: Int = 0;

   var ramsch: Int = 0;
   var ramschPercent: Int = 0;

   var customGames: Int = 0;
   var customGamesPercent: Int = 0;

   init {

   }

   fun addStatisticsListener(listener: IStatisticsListener) {
      listeners.add(listener)
   }

   private fun recalculate(game: Game?){
      if(game == null) return

      reset()

      this.game = game
      var rounds = game.lstRounds

      updateGameCounter(rounds)

      for(i in rounds.indices){
         var round = rounds[i]

         updateGameTypeCounter(round)



      }

      updateGameTypePercentages()
      notifyStatisticsUpdated()
   }

   private fun reset() {
      roundsPlayed = 0
      normalGames = 0
      solos = 0
      wenzen = 0
      ramsch = 0
      customGames = 0
   }

   private fun updateGameTypePercentages() {

   }

   private fun updateGameTypeCounter(round: GameRound) {
      when(round.gameMode.name){
         GameController.ID_GAME_MODE_DEFAULT -> normalGames++
         GameController.ID_GAME_MODE_SOLO -> solos++
         GameController.ID_GAME_MODE_WENZ -> wenzen++
         GameController.ID_GAME_MODE_RAMSCH -> ramsch++
         GameController.ID_GAME_MODE_CUSTOM -> customGames++
      }
   }

   private fun updateGameCounter(rounds: java.util.ArrayList<GameRound>) {
      roundsPlayed = rounds.size
   }

   private fun notifyStatisticsUpdated() {
      listeners.forEach { it.onStatisticsUpdated() }
   }

   override fun onGameRoundsChanged(game: Game?) {
      recalculate(game)
   }

   override fun onGameCreated(
         game: Game?,
         newGame: Boolean,
         cachedGame: Boolean,
         loadingMessage: String?
   ) {
      recalculate(game)
   }
}