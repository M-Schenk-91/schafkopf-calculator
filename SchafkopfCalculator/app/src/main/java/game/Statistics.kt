package game

import android.util.Log
import ui.interfaces.IGameListener
import ui.interfaces.IStatisticsListener
import java.io.Serializable

object Statistics : Serializable, IGameListener {

   private var listeners: ArrayList<IStatisticsListener> = ArrayList()
   lateinit var game: Game

   var roundsPlayed: Int = 0
   var normalGames: Int = 0
   var solos: Int = 0
   var wenzen: Int = 0
   var ramsch: Int = 0
   var customGames: Int = 0
   var winningCountsPerPlayer: Array<Int> = arrayOf(0, 0, 0, 0)
   var averageScorePerPlayer: Array<Double> = arrayOf(0.00, 0.00, 0.00, 0.00)


   init {
   }

   fun addStatisticsListener(listener: IStatisticsListener) {
      listeners.add(listener)
   }

   private fun recalculate(game: Game?) {
      if (game == null) return

      this.game = game
      reset()

      var rounds = game.lstRounds

      updateGameCounter(rounds)
      updateAverageScore()

      for (i in rounds.indices) {
         var round = rounds[i]

         updateGameTypeCounter(round)
         updateWinnersCounter(round)
      }

      notifyStatisticsUpdated()
   }

   private fun updateAverageScore() {
      game.lstPlayers.forEachIndexed { index, player ->

         if (roundsPlayed > 0) {
            averageScorePerPlayer[index] = (player.score.toDouble() / 100) / roundsPlayed
         }
      }
   }

   private fun updateWinnersCounter(round: GameRound?) {
      for (i in round?.lstWinners?.indices!!) {
         if (round?.lstWinners[i]) {
            winningCountsPerPlayer[i] = winningCountsPerPlayer[i].inc()
         }
      }
   }

   private fun reset() {
      roundsPlayed = 0
      normalGames = 0
      solos = 0
      wenzen = 0
      ramsch = 0
      customGames = 0

      winningCountsPerPlayer = arrayOf(0, 0, 0, 0)
      averageScorePerPlayer = arrayOf(0.00, 0.00, 0.00, 0.00)
   }

   private fun updateGameTypeCounter(round: GameRound) {
      when (round.gameMode.name) {
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