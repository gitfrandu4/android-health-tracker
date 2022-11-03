package com.example.android_health_tracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_health_tracker.R
import com.example.android_health_tracker.models.records.Water
import java.text.DateFormat

class MyRecordsAdapter (private val myRecords: ArrayList<Water>) :
    RecyclerView.Adapter<MyRecordsAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val tvAmount: TextView = v.findViewById(R.id.tvAmount)
        private val tvDate: TextView = v.findViewById(R.id.tvDate)
        private val tvCategory: TextView = v.findViewById(R.id.tvCategory)


        fun bindData(data: Water) {
            tvAmount.text = data.amount.toString()
            tvDate.text = data.date?.let {
                DateFormat.getDateInstance().format(it)
            }
            tvCategory.text = data.category.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val data = myRecords[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return myRecords.size
    }
}