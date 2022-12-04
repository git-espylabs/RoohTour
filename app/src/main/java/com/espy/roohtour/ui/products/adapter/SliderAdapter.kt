package com.espy.roohtour.ui.products.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.espy.roohtour.R
import com.espy.roohtour.app.AppSettings
import com.espy.roohtour.databinding.FragmentImageSliderBinding
import com.espy.roohtour.ui.products.models.ImageSlide
import java.io.File

class SliderAdapter(
    private val context: Context,
    private val slides: List<ImageSlide>
) : RecyclerView.Adapter<SliderAdapter.PageHolder>(){

    inner class PageHolder(val binding: FragmentImageSliderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
        return PageHolder(
            FragmentImageSliderBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
        val data = slides[position]

        try {
            val imgFile = File(context.getExternalFilesDir(null).toString() + "/CapFiles/", data.imageName)
            if (imgFile.exists()){
                val myBitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                holder.binding.imageGraphics.setImageBitmap(myBitmap);

            }else{
                Glide
                    .with(context)
                    .load(AppSettings.endPoints.IMAGE_ASSETS + data.imageName)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.binding.imageGraphics)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = slides.size
}