package com.espy.roohtour.ui.shops.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espy.roohtour.R
import com.espy.roohtour.api.models.shops.FollowupHistoryItem
import com.espy.roohtour.utils.CommonUtils

class FollowupHistoryAdapter internal constructor(private val context: Context, private val mData: List<FollowupHistoryItem>, val clickHandler: (shop: FollowupHistoryItem) -> Unit) : RecyclerView.Adapter<FollowupHistoryAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var followupDate: TextView = itemView.findViewById(R.id.followupDate)
        internal var confChance: TextView = itemView.findViewById(R.id.confChance)
        internal var ammendDate: TextView = itemView.findViewById(R.id.ammendDate)
        internal var tvNotes: TextView = itemView.findViewById(R.id.tvNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_followup_history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]

        holder.followupDate.text = "Followup Date: " + CommonUtils.getConvertedDate2(data.followup_date?:"")

        val confCance = when(data.confirm_chance){
            "0" -> "Enquiry Level"
            "1" -> "Confirmed"
            "2" -> "High Chance"
            "3" -> "Medium Chance"
            "4" -> "Dropped"
            else -> {"NA"}
        }
        holder.confChance.text = "Confirmation Chance: $confCance"

        holder.ammendDate.text = "Amendment Date: " + CommonUtils.getConvertedDate2(data.amendment_replied_date?:"")

        holder.tvNotes.text = "Notes: " + data.notes

        holder.itemView.setOnClickListener {
            clickHandler(data)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}