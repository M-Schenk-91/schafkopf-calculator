package ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import com.schenk.matthias.schafkopfcalculator.R
import game.GameMode

class GameModeAdapter(
      val dataSet: ArrayList<GameMode>,
      internal var mContext: Context
) : ArrayAdapter<GameMode>(mContext, R.layout.list_item_game_mode, dataSet), View.OnClickListener, View.OnFocusChangeListener {

   private var lastPosition = -1

   // View lookup cache
   private class ViewHolder {
      internal var cbxActive: CheckBox? = null
      internal var edtValue: EditText? = null
   }

   override fun onClick(v: View) {

      val position = v.tag as Int
      val `object` = getItem(position)
      val dataModel = `object` as GameMode

      when (v.getId()) {
         R.id.cbx_is_active -> {
            //(v as CheckBox).isChecked = !v.isChecked
            dataSet[position].isActive = (v as CheckBox).isChecked
            Log.d("", dataSet.toString())
         }
      }
   }

   override fun onFocusChange(v: View?, hasFocus: Boolean) {
      if(!hasFocus){
         (v?.tag as Int?)?.let {
            dataSet[it].value = (v as EditText).text.toString().toInt()

/*
            Toast.makeText(
                  context, dataSet[it].value.toString(),
                  Toast.LENGTH_SHORT
            ).show()
*/

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
         convertView = inflater.inflate(R.layout.list_item_game_mode, parent, false)
         viewHolder.cbxActive = convertView!!.findViewById(R.id.cbx_is_active)
         viewHolder.edtValue = convertView.findViewById(R.id.edt_value)

         convertView.tag = viewHolder
      } else {
         viewHolder = convertView.tag as ViewHolder
      }
      lastPosition = position

      viewHolder.cbxActive!!.text = dataModel!!.name
      viewHolder.cbxActive!!.isChecked = dataModel.isActive
      viewHolder.cbxActive!!.tag = position
      viewHolder.cbxActive!!.setOnClickListener(this)
      viewHolder.edtValue!!.setText(dataModel.value.toString())
      viewHolder.edtValue!!.tag = position
      viewHolder.edtValue!!.onFocusChangeListener = this

      // Return the completed view to render on screen
      return convertView
   }
}
