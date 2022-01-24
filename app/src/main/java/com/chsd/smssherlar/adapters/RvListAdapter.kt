package com.chsd.smssherlar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chsd.smssherlar.R
import com.chsd.smssherlar.models.Data
import kotlinx.android.synthetic.main.list_item.view.*

class RvListAdapter(var list: ArrayList<Data>,var onclick:onClick) : RecyclerView.Adapter<RvListAdapter.Vh>() {
    inner class Vh(var itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun Bind(data: Data) {
            itemview.list_img.setImageResource(data.iamge)
            itemview.list_text.text = data.name
            itemview.setOnClickListener {
                onclick.Onclick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.Bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface onClick{
        fun Onclick(data: Data)
    }
}