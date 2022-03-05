package com.example.mysevenminuteapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysevenminuteapp.databinding.ItemHistoryBinding

class HistoryAdapter(private val items:ArrayList<String>):RecyclerView.Adapter<HistoryAdapter.viewHolder>() {

    class viewHolder(binding: ItemHistoryBinding):RecyclerView.ViewHolder(binding.root) {
        val cons=binding.cons
        val tvHistory=binding.tvItemHistory
        val tvPosition=binding.tvPosition


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val date:String=items.get(position)
        holder.tvPosition.text=(position+1).toString()
        holder.tvHistory .text=date


        if (position % 2==0){
            holder.cons.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }else{
            holder.cons.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}