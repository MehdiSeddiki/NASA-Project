package fr.epita.nasaproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaproject.R
import fr.epita.nasaproject.`object`.EonetObject

class EonetAdapter(val data : EonetObject, val context : Context): RecyclerView.Adapter<EonetAdapter.EonetHolder>(){
    class EonetHolder(rowView : View) : RecyclerView.ViewHolder(rowView){
        val title : TextView = rowView.findViewById(R.id.eonet_list_item_textview_title)
        val type : TextView = rowView.findViewById(R.id.eonet_list_item_textview_type)
        val date : TextView = rowView.findViewById(R.id.eonet_list_item_textview_date)
        val magnitude : TextView = rowView.findViewById(R.id.eonet_list_item_textview_magnitude)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EonetHolder {
        val viewholder : EonetHolder
        val rowView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.eonet_list_item, parent, false)
        viewholder = EonetHolder(rowView)
        return viewholder
    }

    override fun onBindViewHolder(holder: EonetHolder, position: Int) {
        holder.title.text = data.events[position].title
        holder.type.text = data.events[position].categories[0].title
        holder.date.text = data.events[position].geometry[0].date.toString()
        val unit = data.events[position].geometry[0].magnitudeUnit
        when {
            unit == null -> holder.magnitude.text =
                "N/A"
            unit.contains("kts") -> holder.magnitude.text =
                "${data.events[position].geometry[0].magnitudeValue * 0.514444f} m/s"
            unit.contains("NM^2") -> holder.magnitude.text =
                "${data.events[position].geometry[0].magnitudeValue * 1.852f} km"
            unit.contains("Mw") -> holder.magnitude.text =
                "${data.events[position].geometry[0].magnitudeValue} Mw"
        }
    }

    override fun getItemCount(): Int {
        return data.events.size
    }
}