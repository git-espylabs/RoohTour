package com.espy.roohtour.ui.shops.adapter

import android.content.Context
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.espy.roohtour.R
import com.espy.roohtour.app.AppSettings
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.api.models.shops.Shop

class ShopsListAdapter  internal constructor(private val context: Context, private val mData: List<Shop>, val clickHandler: (shop: Shop, isPromptingInOutAction: Boolean) -> Unit, val callHandler: (phoneNumber: String) -> Unit) : RecyclerView.Adapter<ShopsListAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var shopImg: ImageView = itemView.findViewById(R.id.shImage)
        internal var shopName: TextView = itemView.findViewById(R.id.shName)
        internal var shopLocation: TextView = itemView.findViewById(R.id.shLocation)
        internal var shopTime: TextView = itemView.findViewById(R.id.shTime)
        internal var shopinout: Button = itemView.findViewById(R.id.shopinout)
        internal var shRoute: TextView = itemView.findViewById(R.id.shRoute)
        internal var shPhone: TextView = itemView.findViewById(R.id.shPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_shops_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopData = mData[position]

        holder.shopName.text = shopData.shop_name
        holder.shopLocation.text = shopData.shop_address?: ""
        holder.shopTime.text = ""
        holder.shRoute.text = "Route: " + shopData.route_name
        holder.shPhone.text = shopData.shop_primary_number

        Glide
            .with(context)
            .load(AppSettings.endPoints.IMAGE_ASSETS+shopData.shop_image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.shopImg)

        if (AppPreferences.shopInId == shopData.id){
            holder.shopinout.background = ContextCompat.getDrawable(context, R.drawable.bg_cap_button)
            holder.shopinout.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.shopinout.text = context.getString(R.string.shop_out)
        }else{
            holder.shopinout.background = ContextCompat.getDrawable(context, R.drawable.bg_orange_border_white_filled_curver_corner_rect)
            holder.shopinout.setTextColor(ContextCompat.getColor(context, R.color.app_accent_color))
            holder.shopinout.text = context.getString(R.string.shop_in)
        }

        holder.shopinout.setOnClickListener {
            clickHandler(shopData, true)
        }

        holder.itemView.setOnClickListener {
            clickHandler(shopData, false)
        }

        holder.shPhone.setOnClickListener {
            shopData.shop_primary_number?.let { phone ->
                if (phone.isNotEmpty() && isValidPhone(phone)) {
                    callHandler(phone)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    private fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

}