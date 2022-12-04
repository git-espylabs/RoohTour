package com.espy.roohtour.ui.order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espy.roohtour.R
import com.espy.roohtour.ui.order.models.OrderProduct

class OrderSummaryAdapter  internal constructor(private val context: Context, private val mData: ArrayList<OrderProduct?>, val clickHandler: (product: OrderProduct?, isDelete: Boolean, position: Int) -> Unit) : RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvPdtName: TextView = itemView.findViewById(R.id.tvPdtName)
        internal var tvPdtQty: TextView = itemView.findViewById(R.id.tvPdtQty)
        internal var btnAdd: ImageView = itemView.findViewById(R.id.btnAdd)
        internal var tvPdtPrice: TextView = itemView.findViewById(R.id.tvPdtPrice)
        internal var btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        internal var tvPdtTotal: TextView = itemView.findViewById(R.id.tvPdtTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_order_summary_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productData = mData[position]

        productData?.run {
            holder.tvPdtName.text = "$product_name - $batchcode"
            try {
                holder.tvPdtPrice.text = context.getString(R.string.amount_rep_float, product_price.toFloat())
            } catch (e: Exception) {
            }

            if (productData.qty.isNullOrEmpty().not()) {
                holder.tvPdtQty.text = qty
            } else {
                holder.tvPdtQty.text = ""
            }

            holder.btnAdd.setOnClickListener {
                clickHandler(this, false, position)
            }

            holder.btnDelete.setOnClickListener {
                clickHandler(this, true, position)
            }

            holder.tvPdtTotal.text = context.getString(R.string.amount_rep_float,
                (qty.toInt() * product_price.toFloat())
            )
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun updateList(list: ArrayList<OrderProduct?>){
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }
}