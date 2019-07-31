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
    var geier: Int = 0
    var wedding: Int = 0
    var coloredWenz: Int = 0
    var coloredGeier: Int = 0
    var bettel: Int = 0
    var winningCountsPerPlayer: Array<Int> = arrayOf(0, 0, 0, 0)
    var averageScorePerPlayer: Array<Double> = arrayOf(0.00, 0.00, 0.00, 0.00)
    var scoreProgressPlayer1: ArrayList<Int> = ArrayList()
    var scoreProgressPlayer2: ArrayList<Int> = ArrayList()
    var scoreProgressPlayer3: ArrayList<Int> = ArrayList()
    var scoreProgressPlayer4: ArrayList<Int> = ArrayList()
    var scoreProgress: ArrayList<ArrayList<Int>> = ArrayList()


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
            updateScoreProgress(round)
        }

        notifyStatisticsUpdated()
    }

    private fun updateScoreProgress(round: GameRound?) {

        for (i in 0 until round?.numPlayers!!) {

            var changes = scoreProgress.get(i)
            var lastElement = 0
            if (changes.size > 0) lastElement = changes.get(changes.size - 1)

            changes.add(lastElement + round.getRoundScoreChangeForPlayer(i))
        }

        Log.d("fsad", "afdas")
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
        geier = 0
        wedding = 0
        coloredWenz = 0
        coloredGeier = 0
        bettel = 0

        winningCountsPerPlayer = arrayOf(0, 0, 0, 0)
        averageScorePerPlayer = arrayOf(0.00, 0.00, 0.00, 0.00)

        scoreProgressPlayer1.clear()
        scoreProgressPlayer2.clear()
        scoreProgressPlayer3.clear()
        scoreProgressPlayer4.clear()

        scoreProgress.clear()

        scoreProgress.add(scoreProgressPlayer1)
        scoreProgress.add(scoreProgressPlayer2)
        scoreProgress.add(scoreProgressPlayer3)
        scoreProgress.add(scoreProgressPlayer4)

    }

    private fun updateGameTypeCounter(round: GameRound) {
        when (round.gameMode.name) {
            GameController.ID_GAME_MODE_DEFAULT -> normalGames++
            GameController.ID_GAME_MODE_SOLO -> solos++
            GameController.ID_GAME_MODE_WENZ -> wenzen++
            GameController.ID_GAME_MODE_RAMSCH -> ramsch++
            GameController.ID_GAME_MODE_CUSTOM -> customGames++
            GameController.ID_GAME_MODE_GEIER -> geier++
            GameController.ID_GAME_MODE_WEDDING -> wedding++
            GameController.ID_GAME_MODE_COLORED_WENZ -> coloredWenz++
            GameController.ID_GAME_MODE_COLORED_GEIER -> coloredGeier++
            GameController.ID_GAME_MODE_BETTEL -> bettel++
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