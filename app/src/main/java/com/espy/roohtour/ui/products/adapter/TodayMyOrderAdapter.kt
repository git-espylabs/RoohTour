package com.espy.roohtour.ui.products.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espy.roohtour.R
import com.espy.roohtour.ui.products.models.TodayMyOrder

class TodayMyOrderAdapter  internal constructor(private val context: Context, private val mData: List<TodayMyOrder?>, val clickHandler: (product: TodayMyOrder?) -> Unit) : RecyclerView.Adapter<TodayMyOrderAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvPdtName: TextView = itemView.findViewById(R.id.tvPdtName)
        internal var tvPdtCount: TextView = itemView.findViewById(R.id.tvPdtCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_today_my_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productData = mData[position]

        productData?.run{
            holder.tvPdtName.text = product_name
            holder.tvPdtCount.text = totalqty
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}