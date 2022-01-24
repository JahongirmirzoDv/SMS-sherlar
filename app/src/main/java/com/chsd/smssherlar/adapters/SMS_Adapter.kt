package com.chsd.smssherlar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chsd.smssherlar.R
import com.chsd.smssherlar.models.SMS
import kotlinx.android.synthetic.main.sms_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class SMS_Adapter(var list: ArrayList<SMS>, var onclick: onClick, var del:Delete) :
    RecyclerView.Adapter<SMS_Adapter.Vh>(){

    inner class Vh(var itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun Bind(data: SMS, position: Int) {
            itemview.sher_name.text = data.name
            itemview.sher_content.text = data.sms_content
            itemview.like_img.setImageResource(data.like_image)
            itemview.delete_img.setImageResource(data.delete)
            itemview.setOnClickListener {
                onclick.SMSOnclick(data)
            }
            itemview.delete_img.setOnClickListener {
                del.Del(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.sms_item, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.Bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface onClick {
        fun SMSOnclick(data: SMS)
    }

    interface Delete {
        fun Del(position: Int)
    }

}