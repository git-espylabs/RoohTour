package com.espy.roohtour.ui.shops.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espy.roohtour.R
import com.espy.roohtour.api.models.shops.DeliveryShop
import com.espy.roohtour.api.models.shops.EnquiryAgencyItem

class DeliveryShopListAdapter internal constructor(private val context: Context, private val mData: List<EnquiryAgencyItem>, val clickHandler: (shop: EnquiryAgencyItem, navigate: Boolean) -> Unit) : RecyclerView.Adapter<DeliveryShopListAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var shName: TextView = itemView.findViewById(R.id.shName)
        internal var shRoute: TextView = itemView.findViewById(R.id.shRoute)
        internal var shAddress: TextView = itemView.findViewById(R.id.shAddress)
        internal var shLoc: TextView = itemView.findViewById(R.id.shLoc)
        internal var go: ImageView = itemView.findViewById(R.id.go)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_delivery_shop_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]

        holder.shName.text = data.shop_name

        holder.shAddress.text = "Address: " + data.shop_address

        holder.shRoute.text = "Route: " + data.route_name

        holder.shLoc.text = "Lat: " + data.lattitude + ", Lon: " + data.longitude

        holder.shLoc.setOnClickListener {
            clickHandler(data, true)
        }

        holder.go.setOnClickListener {
            clickHandler(data, false)
        }

        holder.itemView.setOnClickListener {
            clickHandler(data, false)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}