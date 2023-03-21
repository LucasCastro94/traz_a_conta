package com.example.trazaconta.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.example.trazaconta.R
import com.example.trazaconta.entity.Item



class ListItemViewAdapter(val context: Context, private val clickView : onClick) :
    RecyclerView.Adapter<ListItemViewAdapter.ViewHolder>() {

    private var item: MutableList<Item> = mutableListOf()

    override fun getItemCount(): Int {
        return item.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = item[position].nameitem
        holder.price.text =String.format("R$ %.2f",item[position].price)
        holder.fk_establishment =item[position].fk_Establishment

        holder.itemView.setOnClickListener {
        popupMenu(holder.itemView,item[position])
        }
    }

    private fun popupMenu(view:View, item: Item)
    {
        val popupMenu = PopupMenu(context,view)
        popupMenu.menu.add(Menu.NONE,0,0,"Editar")
        popupMenu.menu.add(Menu.NONE,1,1,"Remover")

        popupMenu.setOnMenuItemClickListener { menu ->
            when(menu.itemId)
            {
                0->{
                   clickView.update(item)
                }
                1->{
                  clickView.delete(item)
                }
            }
            false
         }

        popupMenu.show()

    }

    interface onClick
    {
        fun delete(item : Item)
        fun update(item : Item)
    }



    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Item>) {
        item.clear()
        item.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     var name : TextView
     var price: TextView
     var fk_establishment : Long

        init {
            name = itemView.findViewById(R.id.itemlist_name_id)
            price = itemView.findViewById(R.id.itemlist_price_id)
            fk_establishment =0L

        }

    }

}