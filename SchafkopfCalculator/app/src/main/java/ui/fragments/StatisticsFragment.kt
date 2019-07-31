package ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.schenk.matthias.schafkopfcalculator.R
import game.Statistics
import kotlinx.android.synthetic.main.fragment_statistics.*
import ui.AppColors
import ui.UiUtils
import ui.custom.SchafkopfFragment
import ui.interfaces.IStatisticsListener

var animationMillis: Int = 500


class StatisticsFragment : SchafkopfFragment(), IStatisticsListener {

   /*
      Framework: https://github.com/PhilJay/MPAndroidChart
    */

   override fun onStatisticsUpdated() {

   }

   override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
   ): View? {
      return inflater?.inflate(R.layout.fragment_statistics, container, false)
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupScore()
      setupPlayedGamesChart()
      setupProgressChart()
      setupAverageScoresChart()
      setupWinningCountsPerPlayerChart()
   }

   private fun setupScore() {
      color_inidcator_player_1.setBackgroundColor(AppColors.PLAYER_COLORS[0])
      color_inidcator_player_2.setBackgroundColor(AppColors.PLAYER_COLORS[1])
      color_inidcator_player_3.setBackgroundColor(AppColors.PLAYER_COLORS[2])
      color_inidcator_player_4.setBackgroundColor(AppColors.PLAYER_COLORS[3])

      if(Statistics.roundsPlayed <= 0) return

      lbl_score_player_1.text = UiUtils.getScoreString(UiUtils.formatScoreValue(Statistics.game.lstPlayers[0].score))
      lbl_score_player_2.text = UiUtils.getScoreString(UiUtils.formatScoreValue(Statistics.game.lstPlayers[1].score))
      lbl_score_player_3.text = UiUtils.getScoreString(UiUtils.formatScoreValue(Statistics.game.lstPlayers[2].score))
      lbl_score_player_4.text = UiUtils.getScoreString(UiUtils.formatScoreValue(Statistics.game.lstPlayers[3].score))

      lbl_player_1.text = Statistics.game.lstPlayers[0].name
      lbl_player_2.text = Statistics.game.lstPlayers[1].name
      lbl_player_3.text = Statistics.game.lstPlayers[2].name
      lbl_player_4.text = Statistics.game.lstPlayers[3].name
   }

   private fun setupProgressChart() {

      chartStatisticsProgress.setNoDataText(context!!.getString(R.string.chart_no_games))
      chartStatisticsProgress.setNoDataTextColor(Color.BLACK)

      if(Statistics.roundsPlayed <= 1) return

      var entriesPlayer1: ArrayList<Entry> = ArrayList()
      var entriesPlayer2: ArrayList<Entry> = ArrayList()
      var entriesPlayer3: ArrayList<Entry> = ArrayList()
      var entriesPlayer4: ArrayList<Entry> = ArrayList()

      var labels: Array<String> = Statistics.game.settings.lstPlayerNames

      for (i in 0 until Statistics.roundsPlayed){
         entriesPlayer1.add(Entry(i.toFloat(), Statistics.scoreProgressPlayer1.get(i).toFloat()))
         entriesPlayer2.add(Entry(i.toFloat(), Statistics.scoreProgressPlayer2.get(i).toFloat()))
         entriesPlayer3.add(Entry(i.toFloat(), Statistics.scoreProgressPlayer3.get(i).toFloat()))
         entriesPlayer4.add(Entry(i.toFloat(), Statistics.scoreProgressPlayer4.get(i).toFloat()))
      }

      var dataset1 = LineDataSet(entriesPlayer1, Statistics.game.lstPlayers[0].name)
      var dataset2 = LineDataSet(entriesPlayer2, Statistics.game.lstPlayers[1].name)
      var dataset3 = LineDataSet(entriesPlayer3, Statistics.game.lstPlayers[2].name)
      var dataset4 = LineDataSet(entriesPlayer4, Statistics.game.lstPlayers[3].name)

      designLineData(dataset1, 0)
      designLineData(dataset2, 1)
      designLineData(dataset3, 2)
      designLineData(dataset4, 3)

      var chartData = LineData()
      chartData.addDataSet(dataset1)
      chartData.addDataSet(dataset2)
      chartData.addDataSet(dataset3)
      chartData.addDataSet(dataset4)

      /*
      chartData.barWidth = 0.5f
      chartData.setValueFormatter(object : IValueFormatter {
         override fun getFormattedValue(
                 value: Float,
                 entry: Entry?,
                 dataSetIndex: Int,
                 viewPortHandler: ViewPortHandler?
         ): String {
            return UiUtils.getScoreString(value.toDouble())
         }

      })
*/

      chartStatisticsProgress.setDrawGridBackground(true)
      chartStatisticsProgress.animateXY(animationMillis, animationMillis)
      chartStatisticsProgress.description.isEnabled = false
      chartStatisticsProgress.setTouchEnabled(false)
      //chartStatisticsProgress.setDrawValueAboveBar(false)

      val formatter = object : IAxisValueFormatter {
         val decimalDigits: Int
            get() = 0

         override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return UiUtils.getScoreString(UiUtils.formatScoreValue(value.toInt())).toString()
         }
      }


      //chartStatisticsAverageScores.xAxis.setGranularity(1f)
      //chartStatisticsAverageScores.xAxis.setValueFormatter(formatter)
      //chartStatisticsProgress.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
      chartStatisticsProgress.xAxis.setDrawGridLines(false)
      chartStatisticsProgress.xAxis.setDrawLabels(false)
      chartStatisticsProgress.xAxis.setDrawAxisLine(false)

      chartStatisticsProgress.axisLeft.setDrawGridLines(true)
      chartStatisticsProgress.axisLeft.setValueFormatter(formatter)
      //chartStatisticsProgress.axisLeft.setDrawLabels(false)
      //chartStatisticsProgress.axisLeft.setDrawAxisLine(false)

      chartStatisticsProgress.axisRight.setDrawGridLines(false)
      chartStatisticsProgress.axisRight.setDrawLabels(false)
      chartStatisticsProgress.axisRight.setDrawAxisLine(false)

      chartStatisticsProgress.data = chartData

   }

   private fun designLineData(dataset: LineDataSet, num: Int) {
      dataset.color = AppColors.PLAYER_COLORS[num]
      dataset.setDrawValues(false)
      dataset.setDrawCircles(false)
      dataset.lineWidth = 4f
      dataset.mode = LineDataSet.Mode.CUBIC_BEZIER


   }

   private fun setupAverageScoresChart() {
      var entries: ArrayList<BarEntry> = ArrayList()
      var labels: Array<String> = Statistics.game.settings.lstPlayerNames

      Statistics.averageScorePerPlayer.forEachIndexed { index, i ->
         entries.add(BarEntry(index.toFloat().inc(), i.toFloat()))
      }

      var dataset = BarDataSet(entries, "")
      dataset.colors = AppColors.PLAYER_COLORS.toMutableList()
      dataset.valueTextSize = 16F

      var barData = BarData(dataset)
      barData.barWidth = 0.4f

      barData.setValueFormatter(object : IValueFormatter {
         override fun getFormattedValue(
               value: Float,
               entry: Entry?,
               dataSetIndex: Int,
               viewPortHandler: ViewPortHandler?
         ): String {
            return UiUtils.getScoreString(value.toDouble())
         }

      })

      chartStatisticsAverageScores.setDrawGridBackground(true)
      chartStatisticsAverageScores.animateXY(animationMillis, animationMillis)
      chartStatisticsAverageScores.setNoDataText(context!!.getString(R.string.chart_no_games))
      chartStatisticsAverageScores.legend.isEnabled = false
      chartStatisticsAverageScores.description.isEnabled = false
      chartStatisticsAverageScores.setTouchEnabled(false)
      chartStatisticsAverageScores.setDrawValueAboveBar(false)

      val formatter = object : IAxisValueFormatter {
         val decimalDigits: Int
            get() = 0

         override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return labels[value.toInt() - 1]
         }
      }

      chartStatisticsAverageScores.xAxis.setGranularity(1f)
      chartStatisticsAverageScores.xAxis.setValueFormatter(formatter)
      chartStatisticsAverageScores.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
      chartStatisticsAverageScores.xAxis.setDrawGridLines(true)

      chartStatisticsAverageScores.axisLeft.setDrawGridLines(true)
      chartStatisticsAverageScores.axisLeft.setDrawLabels(false)
      chartStatisticsAverageScores.axisLeft.setDrawAxisLine(false)

      chartStatisticsAverageScores.axisRight.setDrawGridLines(false)
      chartStatisticsAverageScores.axisRight.setDrawLabels(false)
      chartStatisticsAverageScores.axisRight.setDrawAxisLine(false)

      chartStatisticsAverageScores.data = barData

   }

   private fun setupWinningCountsPerPlayerChart() {

      chartStatisticsWinningCounts.setNoDataText(context!!.getString(R.string.chart_no_games))
      chartStatisticsWinningCounts.setNoDataTextColor(Color.BLACK)
      if(Statistics.roundsPlayed <= 1) return

      var entries: ArrayList<BarEntry> = ArrayList()
      var labels: Array<String> = Statistics.game.settings.lstPlayerNames

      Statistics.winningCountsPerPlayer.forEachIndexed { index, i ->
         entries.add(BarEntry(index.toFloat().inc(), i.toFloat()))
      }

      var dataset = BarDataSet(entries, "")
      dataset.colors = AppColors.PLAYER_COLORS.toMutableList()
      dataset.valueTextSize = 16F

      var barData = BarData(dataset)
      barData.barWidth = 0.4f

      barData.setValueFormatter(object : IValueFormatter {
         override fun getFormattedValue(
               value: Float,
               entry: Entry?,
               dataSetIndex: Int,
               viewPortHandler: ViewPortHandler?
         ): String {
            return value.toInt().toString()
         }
      })

      chartStatisticsWinningCounts.legend.isEnabled = false
      chartStatisticsWinningCounts.description.isEnabled = false
      chartStatisticsWinningCounts.setTouchEnabled(false)
      chartStatisticsWinningCounts.setDrawValueAboveBar(true)
      chartStatisticsWinningCounts.setDrawGridBackground(true)
      chartStatisticsWinningCounts.animateXY(animationMillis, animationMillis)

      val formatter = object : IAxisValueFormatter {
         val decimalDigits: Int
            get() = 0

         override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return labels[value.toInt().dec()]
         }
      }

      chartStatisticsWinningCounts.xAxis.setGranularity(1f)
      chartStatisticsWinningCounts.xAxis.setValueFormatter(formatter)
      chartStatisticsWinningCounts.xAxis.setDrawGridLines(true)
      chartStatisticsWinningCounts.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
      chartStatisticsWinningCounts.getAxisLeft().setAxisMinimum(0.toFloat())

      chartStatisticsWinningCounts.axisLeft.setDrawGridLines(false)
      chartStatisticsWinningCounts.axisLeft.setDrawLabels(false)
      chartStatisticsWinningCounts.axisLeft.setDrawAxisLine(false)

      chartStatisticsWinningCounts.axisRight.setDrawGridLines(false)
      chartStatisticsWinningCounts.axisRight.setDrawLabels(false)
      chartStatisticsWinningCounts.axisRight.setDrawAxisLine(false)

      chartStatisticsWinningCounts.data = barData
   }

   private fun setupPlayedGamesChart() {
      var entries: ArrayList<PieEntry> = ArrayList()

      if (Statistics.normalGames > 0) entries.add(
            PieEntry(
                  Statistics.normalGames.toFloat(),
                  "Sauspiel"
            )
      )

      if (Statistics.solos > 0) entries.add(PieEntry(Statistics.solos.toFloat(), "Solo"))
      if (Statistics.wenzen > 0) entries.add(PieEntry(Statistics.wenzen.toFloat(), "Wenz"))
      if (Statistics.ramsch > 0) entries.add(PieEntry(Statistics.ramsch.toFloat(), "Ramsch"))
      if (Statistics.geier > 0) entries.add(PieEntry(Statistics.geier.toFloat(), "Geier"))
      if (Statistics.wedding > 0) entries.add(PieEntry(Statistics.wedding.toFloat(), "Hochzeit"))
      if (Statistics.coloredWenz > 0) entries.add(PieEntry(Statistics.coloredWenz.toFloat(), "Farbwenz"))
      if (Statistics.coloredGeier > 0) entries.add(PieEntry(Statistics.coloredGeier.toFloat(), "Farbgeier"))
      if (Statistics.bettel > 0) entries.add(PieEntry(Statistics.bettel.toFloat(), "Bettel"))
      if (Statistics.customGames > 0) entries.add(
            PieEntry(
                  Statistics.customGames.toFloat(),
                  "Manuelle Eingabe"
            )
      )

      var dataset = PieDataSet(entries, "LABEL")
      dataset.colors = AppColors.STATISTICS_COLORS.toMutableList()
      dataset.valueTextSize = 16F

      var pieData = PieData(dataset)
      pieData.setValueTextColor(Color.WHITE)
      pieData.setValueFormatter(object : IValueFormatter {
         override fun getFormattedValue(
               value: Float,
               entry: Entry?,
               dataSetIndex: Int,
               viewPortHandler: ViewPortHandler?
         ): String {
            return value.toInt().toString() + " %"
         }

      })

      chartStatisticsGamesPlayed.setTouchEnabled(false)
      chartStatisticsGamesPlayed.centerText = Statistics.roundsPlayed.toString()
      chartStatisticsGamesPlayed.setCenterTextSize(30F)
      chartStatisticsGamesPlayed.setNoDataText(context!!.getString(R.string.chart_no_games))
      chartStatisticsGamesPlayed.setDrawEntryLabels(true)
      chartStatisticsGamesPlayed.setUsePercentValues(true)
      chartStatisticsGamesPlayed.setEntryLabelColor(Color.WHITE)
      chartStatisticsGamesPlayed.setEntryLabelTextSize(10f)
      chartStatisticsGamesPlayed.legend.isEnabled = false
      chartStatisticsGamesPlayed.description.isEnabled = false
      chartStatisticsGamesPlayed.holeRadius = 30.toFloat()
      chartStatisticsGamesPlayed.transparentCircleRadius = 35.toFloat()
      chartStatisticsGamesPlayed.animateXY(animationMillis, animationMillis)
      chartStatisticsGamesPlayed.data = pieData
   }
}