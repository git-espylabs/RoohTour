package com.espy.roohtour.ui.products.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espy.roohtour.R
import com.espy.roohtour.api.models.products.Category
import com.espy.roohtour.extensions.getCompatColor

class CategoryListAdapter  internal constructor(private val context: Context, private val mData: List<Category>, val clickHandler: (categoryId: String?) -> Unit) : RecyclerView.Adapter<CategoryListAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    var rowIndex = 0

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var catName: TextView = itemView.findViewById(R.id.catName)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_categories_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]

        holder.catName.text = data.category_name

        if(rowIndex == position){
            holder.catName.setBackgroundResource(R.drawable.bg_simple_text_bg_orange_fill)
            holder.catName.setTextColor(context.getCompatColor(R.color.white))
        }else{
            holder.catName.setBackgroundResource(R.drawable.bg_simple_text_bg_orange_stroke)
            holder.catName.setTextColor(context.getCompatColor(R.color.app_accent_color))
        }

        holder.itemView.setOnClickListener {
            rowIndex = position
            clickHandler(data.id)
            notifyDataSetChanged()
        }
    }
}