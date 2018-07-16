package ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ViewPortHandler
import com.schenk.matthias.schafkopfcalculator.R
import game.Statistics
import ui.custom.SchafkopfFragment
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.fragment_statistics.view.chartStatisticsWinningCounts
import ui.interfaces.IStatisticsListener
import java.util.logging.Logger
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.schenk.matthias.schafkopfcalculator.R.id.chartStatisticsGamesPlayed
import ui.UiUtils

var animationMillis: Int = 1000



class StatisticsFragment : SchafkopfFragment (), IStatisticsListener {

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
      //txtStatisticsRoundsPlayedVal.text = Statistics.roundsPlayed.toString()

      setupPlayedGamesChart()
      setupWinningCountsPerPlayerChart()
      setupAverageScoresChart()

   }

   private fun setupAverageScoresChart() {
      var entries: ArrayList<BarEntry> = ArrayList()

      Statistics.averageScorePerPlayer.forEachIndexed { index, i ->

         entries.add(BarEntry(index.toFloat().inc(), i.toFloat()))
      }

      var dataset = BarDataSet(entries, "LABEL")
      dataset.colors = ColorTemplate.LIBERTY_COLORS.toMutableList()
      dataset.valueTextSize = 16F

      var pieData = BarData(dataset)
      pieData.barWidth = 0.5f
      pieData.setValueFormatter(object: IValueFormatter {
         override fun getFormattedValue(
               value: Float,
               entry: Entry?,
               dataSetIndex: Int,
               viewPortHandler: ViewPortHandler?
         ): String {
            return UiUtils.getScoreString(value.toDouble())
         }

      })

      chartStatisticsAverageScores.setNoDataText(context.getString(R.string.chart_no_games))
      chartStatisticsAverageScores.legend.isEnabled = false;
      chartStatisticsAverageScores.description.isEnabled = false
      chartStatisticsAverageScores.setTouchEnabled(false)
      chartStatisticsAverageScores.setDrawValueAboveBar(false)


      //chartStatisticsAverageScores.xAxis.axisMaximum = 5.toFloat()
      //chartStatisticsAverageScores.xAxis.axisMinimum = 0.toFloat()
      chartStatisticsAverageScores.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

      chartStatisticsAverageScores.xAxis.setDrawGridLines(false)
      //chartStatisticsAverageScores.xAxis.setDrawLabels(false)
      //chartStatisticsAverageScores.xAxis.setDrawAxisLine(false)


      chartStatisticsAverageScores.axisLeft.setDrawGridLines(false)
      chartStatisticsAverageScores.axisLeft.setDrawLabels(false)
      chartStatisticsAverageScores.axisLeft.setDrawAxisLine(false)

      chartStatisticsAverageScores.axisRight.setDrawGridLines(false)
      chartStatisticsAverageScores.axisRight.setDrawLabels(false)
      chartStatisticsAverageScores.axisRight.setDrawAxisLine(false)

      chartStatisticsAverageScores.setDrawGridBackground(false)
      chartStatisticsAverageScores.animateXY(animationMillis, animationMillis)
      chartStatisticsAverageScores.data = pieData
   }

   private fun setupWinningCountsPerPlayerChart() {
      var entries: ArrayList<BarEntry> = ArrayList()

      Statistics.winningCountsPerPlayer.forEachIndexed { index, i ->

         entries.add(BarEntry(index.toFloat().inc(), i.toFloat()))
      }

      var dataset = BarDataSet(entries, "LABEL")
      dataset.colors = ColorTemplate.LIBERTY_COLORS.toMutableList()
      dataset.valueTextSize = 16F

      var pieData = BarData(dataset)
      pieData.barWidth = 0.5f
      pieData.setValueFormatter(object: IValueFormatter {
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
      chartStatisticsWinningCounts.legend.isEnabled = false;
      chartStatisticsWinningCounts.description.isEnabled = false
      chartStatisticsWinningCounts.setTouchEnabled(false)
      chartStatisticsWinningCounts.setDrawValueAboveBar(false)

      //chartStatisticsWinningCounts.xAxis.axisMaximum = 5.toFloat()
      //chartStatisticsWinningCounts.xAxis.axisMinimum = 0.toFloat()

      chartStatisticsWinningCounts.xAxis.setDrawGridLines(false)
      //chartStatisticsWinningCounts.xAxis.setDrawLabels(false)
      //chartStatisticsWinningCounts.xAxis.setDrawAxisLine(false)
      chartStatisticsWinningCounts.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



      //chartStatisticsWinningCounts.axisLeft.axisMaximum = Statistics.roundsPlayed.toFloat()
      //chartStatisticsWinningCounts.axisLeft.axisMinimum = 0.toFloat()

      chartStatisticsWinningCounts.axisLeft.setDrawGridLines(false)
      chartStatisticsWinningCounts.axisLeft.setDrawLabels(false)
      chartStatisticsWinningCounts.axisLeft.setDrawAxisLine(false)

      chartStatisticsWinningCounts.axisRight.setDrawGridLines(false)
      chartStatisticsWinningCounts.axisRight.setDrawLabels(false)
      chartStatisticsWinningCounts.axisRight.setDrawAxisLine(false)

      chartStatisticsWinningCounts.setDrawGridBackground(false)
      chartStatisticsWinningCounts.animateXY(animationMillis, animationMillis)
      chartStatisticsWinningCounts.data = pieData

   }

   private fun setupPlayedGamesChart() {
         var entries: ArrayList<PieEntry> = ArrayList()

         if(Statistics.normalGames > 0) entries.add(PieEntry(Statistics.normalGames.toFloat(), "Sauspiel"))
         if(Statistics.solos > 0) entries.add(PieEntry(Statistics.solos.toFloat(), "Solo"))
         if(Statistics.wenzen > 0) entries.add(PieEntry(Statistics.wenzen.toFloat(), "Wenz"))
         if(Statistics.ramsch > 0) entries.add(PieEntry(Statistics.ramsch.toFloat(), "Ramsch"))
         if(Statistics.customGames > 0) entries.add(PieEntry(Statistics.customGames.toFloat(), "Manuelle Eingabe"))

         var dataset = PieDataSet(entries, "LABEL")
         dataset.colors = ColorTemplate.LIBERTY_COLORS.toMutableList()
         dataset.valueTextSize = 16F

         var pieData = PieData(dataset)
         pieData.setValueFormatter(object: IValueFormatter {
            override fun getFormattedValue(
                  value: Float,
                  entry: Entry?,
                  dataSetIndex: Int,
                  viewPortHandler: ViewPortHandler?
            ): String {
               return value.toInt().toString()
            }

         })

         chartStatisticsGamesPlayed.setTouchEnabled(false)
         chartStatisticsGamesPlayed.centerText = Statistics.roundsPlayed.toString()
         chartStatisticsGamesPlayed.setCenterTextSize(30F)
         chartStatisticsGamesPlayed.setNoDataText(context.getString(R.string.chart_no_games))
         chartStatisticsGamesPlayed.setDrawEntryLabels(true)
         chartStatisticsGamesPlayed.legend.isEnabled = false;
         chartStatisticsGamesPlayed.description.isEnabled = false
         chartStatisticsGamesPlayed.holeRadius = 40.toFloat()
         chartStatisticsGamesPlayed.transparentCircleRadius = 45.toFloat()
         chartStatisticsGamesPlayed.animateXY(animationMillis, animationMillis)
         chartStatisticsGamesPlayed.data = pieData
   }
}