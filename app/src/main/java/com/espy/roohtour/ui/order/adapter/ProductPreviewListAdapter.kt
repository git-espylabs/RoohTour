package com.espy.roohtour.ui.order.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.espy.roohtour.R
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.app.AppSettings
import java.io.File

class ProductPreviewListAdapter  internal constructor(private val context: Context, private val mData: List<Product>, val clickHandler: (product: Product?) -> Unit) : RecyclerView.Adapter<ProductPreviewListAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var productImg: ImageView = itemView.findViewById(R.id.pdtImage)
        internal var productName: TextView = itemView.findViewById(R.id.pdtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_product_preview_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productData = mData[position]

        holder.productName.text = productData.product_name

        try {
            val imgFile = File(context.getExternalFilesDir(null).toString() + "/CapFiles/", productData.id + "_" + productData.thumbnail)
            if (imgFile.exists()){
                val myBitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.productImg.setImageBitmap(myBitmap);

            }else{
                Glide
                    .with(context)
                    .load(AppSettings.endPoints.IMAGE_ASSETS + productData.thumbnail)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.productImg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.itemView.setOnClickListener {
            clickHandler(productData)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}