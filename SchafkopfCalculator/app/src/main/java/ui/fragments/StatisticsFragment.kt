package ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.schenk.matthias.schafkopfcalculator.R
import game.Statistics
import ui.custom.SchafkopfFragment
import kotlinx.android.synthetic.main.fragment_statistics.*
import ui.interfaces.IStatisticsListener

class StatisticsFragment : SchafkopfFragment (), IStatisticsListener {

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
      txtStatisticsRoundsPlayedVal.text = Statistics.roundsPlayed.toString()


      setupChart()

   }

   private fun setupChart() {
         var entries: ArrayList<PieEntry> = ArrayList()


         entries.add(PieEntry(Statistics.normalGames.toFloat(), 0.toFloat()))
         entries.add(PieEntry(Statistics.solos.toFloat(), 1.toFloat()))
         entries.add(PieEntry(Statistics.wenzen.toFloat(), 2.toFloat()))
         entries.add(PieEntry(Statistics.ramsch.toFloat(), 3.toFloat()))
         //entries.add(PieEntry(Statistics.customGames.toFloat(), 4.toFloat()))

         var a = PieDataSet(entries, null)

         a.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
         a.valueTextSize = 20F
         var c: ArrayList<String> = ArrayList()

         var b = PieData(a)


         chart.centerText = Statistics.roundsPlayed.toString()
         chart.setCenterTextSize(24F)
         chart.data = b
   }
}