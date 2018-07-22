package ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ViewPortHandler
import com.schenk.matthias.schafkopfcalculator.R
import game.Statistics
import kotlinx.android.synthetic.main.fragment_statistics.chartStatisticsAverageScores
import kotlinx.android.synthetic.main.fragment_statistics.chartStatisticsGamesPlayed
import kotlinx.android.synthetic.main.fragment_statistics.chartStatisticsWinningCounts
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
         inflater: LayoutInflater?,
         container: ViewGroup?,
         savedInstanceState: Bundle?
   ): View? {
      return inflater?.inflate(R.layout.fragment_statistics, container, false)
   }

   override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupPlayedGamesChart()
      setupAverageScoresChart()
      setupWinningCountsPerPlayerChart()
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
      barData.barWidth = 0.5f
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

      chartStatisticsAverageScores.setDrawGridBackground(false)
      chartStatisticsAverageScores.animateXY(animationMillis, animationMillis)
      chartStatisticsAverageScores.setNoDataText(context.getString(R.string.chart_no_games))
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
      chartStatisticsAverageScores.xAxis.setDrawGridLines(false)

      chartStatisticsAverageScores.axisLeft.setDrawGridLines(false)
      chartStatisticsAverageScores.axisLeft.setDrawLabels(false)
      chartStatisticsAverageScores.axisLeft.setDrawAxisLine(false)

      chartStatisticsAverageScores.axisRight.setDrawGridLines(false)
      chartStatisticsAverageScores.axisRight.setDrawLabels(false)
      chartStatisticsAverageScores.axisRight.setDrawAxisLine(false)

      chartStatisticsAverageScores.data = barData

   }

   private fun setupWinningCountsPerPlayerChart() {
      var entries: ArrayList<BarEntry> = ArrayList()
      var labels: Array<String> = Statistics.game.settings.lstPlayerNames

      Statistics.winningCountsPerPlayer.forEachIndexed { index, i ->
         entries.add(BarEntry(index.toFloat().inc(), i.toFloat()))
      }

      var dataset = BarDataSet(entries, "")
      dataset.colors = AppColors.PLAYER_COLORS.toMutableList()
      dataset.valueTextSize = 16F

      var barData = BarData(dataset)
      barData.barWidth = 0.5f

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

      chartStatisticsWinningCounts.setNoDataText(context.getString(R.string.chart_no_games))
      chartStatisticsWinningCounts.legend.isEnabled = false
      chartStatisticsWinningCounts.description.isEnabled = false
      chartStatisticsWinningCounts.setTouchEnabled(false)
      chartStatisticsWinningCounts.setDrawValueAboveBar(false)
      chartStatisticsWinningCounts.setDrawGridBackground(false)
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
      chartStatisticsWinningCounts.xAxis.setDrawGridLines(false)
      chartStatisticsWinningCounts.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)

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
      chartStatisticsGamesPlayed.setNoDataText(context.getString(R.string.chart_no_games))
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