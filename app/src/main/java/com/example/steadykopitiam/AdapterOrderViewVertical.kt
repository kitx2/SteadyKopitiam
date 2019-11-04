package com.example.steadykopitiam

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

class AdapterOrderViewVertical(ctx: Context, private val imageModelArrayList: ArrayList<ModelOrderVertical>) : RecyclerView.Adapter<AdapterOrderViewVertical.MyViewHolder>() {

    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterOrderViewVertical.MyViewHolder {

        val view = inflater.inflate(R.layout.rv_orderitemvertical, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterOrderViewVertical.MyViewHolder, position: Int) {

        holder.foodname.setText(imageModelArrayList[position].getNames())
        holder.dateOrdered.setText(imageModelArrayList[position].getDates())
        holder.pointValue.setText(imageModelArrayList[position].getPoints())
        holder.totalValue.setText(imageModelArrayList[position].getPrices())
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var dateOrdered: TextView
        var foodname: TextView
        var pointValue: TextView
        var totalValue: TextView

        init {

            dateOrdered = itemView.findViewById(R.id.dateOrdered) as TextView
            foodname = itemView.findViewById(R.id.foodname) as TextView
            pointValue = itemView.findViewById(R.id.pointValue) as TextView
            totalValue = itemView.findViewById(R.id.totalValue) as TextView
        }

    }
}
