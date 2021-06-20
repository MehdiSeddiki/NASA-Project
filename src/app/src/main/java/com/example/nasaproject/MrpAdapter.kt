package com.example.nasaproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MrpAdapter(val data : MrpObject, val context : Context): RecyclerView.Adapter<MrpAdapter.MrpHolder>(){
     class MrpHolder(rowView : View) : RecyclerView.ViewHolder(rowView){
         val camera : TextView = rowView.findViewById<TextView>(R.id.mrp_fragment_list_item_textview_camera)
         val date : TextView = rowView.findViewById<TextView>(R.id.mrp_fragment_list_item_textview_date)
         val rover : TextView = rowView.findViewById<TextView>(R.id.mrp_fragment_list_item_textview_rover)
         val image : ImageView = rowView.findViewById<ImageView>(R.id.mrp_fragment_list_item_img)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MrpHolder {
        val viewholder : MrpHolder
        val rowView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.mrp_list_item, parent, false)
        viewholder = MrpHolder(rowView)
        return viewholder
    }

    override fun onBindViewHolder(holder: MrpHolder, position: Int) {
        holder.rover.text = data.photos[position].rover.name
        holder.camera.text = data.photos[position].camera.name
        holder.date.text = data.photos[position].earth_date.toString()
        val src = data.photos[position].img_src
        Glide.with(context).load(src).into(holder.image);
    }

    override fun getItemCount(): Int {
        return data.photos.size
    }
}