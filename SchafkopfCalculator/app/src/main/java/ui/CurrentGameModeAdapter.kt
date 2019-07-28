package ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.schenk.matthias.schafkopfcalculator.R
import game.GameController
import game.GameMode
import ui.custom.controls.fw.SchafkopfButton
import ui.fragments.dialog.AddNewRoundDialogFragment
import ui.interfaces.IRoundDialogListener
import java.util.ArrayList

class CurrentGameModeAdapter(
      val dataSet: ArrayList<GameMode>,
      internal var mContext: Context,
      val listener: IRoundDialogListener
) : ArrayAdapter<GameMode>(mContext, R.layout.list_item_current_game_mode, dataSet), View.OnClickListener{

   private var lastPosition = -1

   // View lookup cache
   private class ViewHolder {
      internal var btnMode: SchafkopfButton? = null
   }

   override fun onClick(v: View) {

      val position = v.tag as Int
      val `object` = getItem(position)
      val dataModel = `object` as GameMode

      when (v.getId()) {
         R.id.btn_current_game_mode -> {
            listener.onGameModeChanged(dataSet.get(position))
            listener.onPhaseCompleted(AddNewRoundDialogFragment.PHASE_CHOOSE_GAME_MODE, true)
         }
      }
   }

   override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      var convertView = convertView
      // Get the data item for this position
      val dataModel = getItem(position)
      // Check if an existing view is being reused, otherwise inflate the view
      val viewHolder: ViewHolder // view lookup cache stored in tag

      if (convertView == null) {

         viewHolder = ViewHolder()
         val inflater = LayoutInflater.from(context)
         convertView = inflater.inflate(R.layout.list_item_current_game_mode, parent, false)
         viewHolder.btnMode = convertView!!.findViewById(R.id.btn_current_game_mode)

         convertView.tag = viewHolder
      } else {
         viewHolder = convertView.tag as ViewHolder
      }
      lastPosition = position

      if(dataSet[position].name == GameController.ID_GAME_MODE_CUSTOM){
         val shape = GradientDrawable()
         shape.cornerRadius = 16f
         shape.setColor(Color.GRAY)
         viewHolder.btnMode!!.background = shape
      }

      viewHolder.btnMode!!.text = dataModel!!.name
      viewHolder.btnMode!!.tag = position
      viewHolder.btnMode!!.setOnClickListener(this)

      // Return the completed view to render on screen
      return convertView
   }
}
