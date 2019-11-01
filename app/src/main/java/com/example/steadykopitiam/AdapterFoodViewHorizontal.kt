package com.example.steadykopitiam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class AdapterFoodViewHorizontal(ctx: Context, private val imageModelArrayList: ArrayList<ModelFoodHorizontal>) : RecyclerView.Adapter<AdapterFoodViewHorizontal.MyViewHolder>() {

    private val inflater: LayoutInflater

    init {

        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFoodViewHorizontal.MyViewHolder {

        val view = inflater.inflate(R.layout.rv_fooditemhorizontal, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterFoodViewHorizontal.MyViewHolder, position: Int) {

        holder.iv.setImageResource(imageModelArrayList[position].getImage_drawables())
        holder.tv.setText(imageModelArrayList[position].getNames())
        holder.dv.setText(imageModelArrayList[position].getDescriptions())

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var iv: ImageView
        var tv: TextView
        var dv: TextView

        init {
            iv = itemView.findViewById(R.id.iv) as ImageView
            tv = itemView.findViewById(R.id.tv) as TextView
            dv = itemView.findViewById(R.id.dv) as TextView
        }

    }
}
