package com.example.steadykopitiam

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

/**
 * Created by Parsania Hardik on 26-Jun-17.
 */
class AdapterFoodViewVertical(ctx: Context, private val imageModelArrayList: ArrayList<ModelFoodVertical>) : RecyclerView.Adapter<AdapterFoodViewVertical.MyViewHolder>() {

    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFoodViewVertical.MyViewHolder {

        val view = inflater.inflate(R.layout.rv_fooditemvertical, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterFoodViewVertical.MyViewHolder, position: Int) {

        holder.iv.setImageResource(imageModelArrayList[position].getImage_drawables())
        holder.tv.setText(imageModelArrayList[position].getNames())
        holder.dv.setText(imageModelArrayList[position].getDescriptions())
        holder.pv.setText(imageModelArrayList[position].getPrices())
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var iv: ImageView
        var tv: TextView
        var dv: TextView
        var pv: TextView

        init {
            iv = itemView.findViewById(R.id.iv) as ImageView
            tv = itemView.findViewById(R.id.tv) as TextView
            dv = itemView.findViewById(R.id.dv) as TextView
            pv = itemView.findViewById(R.id.pv) as TextView

        }

    }
}
