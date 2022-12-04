package com.espy.roohtour.ui.shops.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espy.roohtour.R
import com.espy.roohtour.api.models.shops.OrderItem

class OrderItemListAdapter internal constructor(private val context: Context, private val mData: List<OrderItem>, val clickHandler: (order: OrderItem) -> Unit) : RecyclerView.Adapter<OrderItemListAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var product: TextView = itemView.findViewById(R.id.product)
        internal var qty: TextView = itemView.findViewById(R.id.qty)
        internal var price: TextView = itemView.findViewById(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_order_item_history_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]

        holder.product.text = data.product_name?:"CAP Product"
        holder.qty.text = data.qty
        holder.price.text = data.total_amount
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}