package com.leony.app.ricknmorty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.leony.app.ricknmorty.R
import com.squareup.picasso.Picasso


class ImageSliderAdapter() :
    RecyclerView.Adapter<ImageSliderAdapter.SliderImageViewHolder>() {

    var imageLists: MutableList<String> = arrayListOf()

    /**
     * An adapter class for the ImageView Slider inside the MainActivity
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_image_list, parent, false)
        return SliderImageViewHolder(view)
    }

    /**
     * Binding item data to the ViewHolder using its position
     */
    override fun onBindViewHolder(holder: SliderImageViewHolder, position: Int) {
        Picasso.get().load(imageLists[position])
            .into(holder.mImageView)
    }

    /**
     * Size of the ImageView's List
     */
    override fun getItemCount(): Int {
        return imageLists.size
    }

    fun imageSliderAdapter(imageList: List<String>) {
        imageLists = imageList as MutableList<String>
    }

    class SliderImageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mImageView by lazy { view.findViewById(R.id.imgCharacterDetail) as ImageView }
    }
}