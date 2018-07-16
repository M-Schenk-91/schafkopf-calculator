package game

import com.github.mikephil.charting.utils.Utils.init
import com.schenk.matthias.schafkopfcalculator.R.menu.settings
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

   var winningCountsPerPlayer: Array<Int> = arrayOf(0,0,0,0)
   var averageScorePerPlayer: Array<Int> = arrayOf(0,0,0,0)


   init {

   }

   fun addStatisticsListener(listener: IStatisticsListener) {
      listeners.add(listener)
   }

   private fun recalculate(game: Game?){
      if(game == null) return


      this.game = game
      reset()

      var rounds = game.lstRounds

      updateGameCounter(rounds)
      updateAverageScore()

      for(i in rounds.indices){
         var round = rounds[i]

         updateGameTypeCounter(round)
         updateWinnersCounter(round)
      }

      updateGameTypePercentages()
      notifyStatisticsUpdated()
   }

   private fun updateAverageScore() {
      game.lstPlayers.forEachIndexed { index, player ->   averageScorePerPlayer[index] = player.score / roundsPlayed}
   }

   private fun updateWinnersCounter(round: GameRound?) {
      for (i in round?.lstWinners?.indices!!){
         if(round?.lstWinners.get(i)){
            winningCountsPerPlayer[i] = winningCountsPerPlayer.get(i).inc()
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

      winningCountsPerPlayer = arrayOf(0,0,0,0)
      averageScorePerPlayer = arrayOf(0,0,0,0)
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