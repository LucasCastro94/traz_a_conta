package com.example.trazaconta.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trazaconta.R
import com.example.trazaconta.entity.Establishment

open class HistoryViewAdapter(val context: Context, private val click : onClickListener,private val callback : EstablishmentListCallback?=null) : RecyclerView.Adapter<HistoryViewAdapter.ViewHolder>() {

    private var establishments : MutableList<Establishment> = mutableListOf()



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)
    }

    interface onClickListener{
        fun delete(establishment: Establishment)
        fun getIDAndName(id:Long,name:String)
    }

    override fun onBindViewHolder(holder: HistoryViewAdapter.ViewHolder, position: Int) {
      holder.name.text = establishments[position].name
        holder.dateHistory.text = establishments[position].date
        holder.hour.text = establishments[position].hour

        holder.itemView.setOnClickListener{
           click.getIDAndName(establishments[position].id,establishments[position].name)
        }
        holder.btn_del.setOnClickListener {
            click.delete(establishments[position])
        }

    }

    override fun getItemCount(): Int {
       return establishments.size
    }

    interface EstablishmentListCallback {
        fun onEstablishmentListUpdated(list: List<Establishment>)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Establishment>) {
        establishments.clear()
        establishments.addAll(newList)
        notifyDataSetChanged()
        callback?.onEstablishmentListUpdated(establishments)


    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView
        var btn_del : ImageButton
        var dateHistory: TextView
        var hour: TextView

        init {
            name = itemView.findViewById(R.id.itemHistory_name_id)
            btn_del = itemView.findViewById(R.id.itemHistory_delete_id)
            dateHistory = itemView.findViewById(R.id.itemHistory_date_id)
            hour = itemView.findViewById(R.id.itemHistory_hour_id)


        }

    }

}