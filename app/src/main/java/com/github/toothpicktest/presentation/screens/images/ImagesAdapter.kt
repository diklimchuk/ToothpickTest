package com.github.toothpicktest.presentation.screens.images

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.toothpicktest.R
import com.github.toothpicktest.presentation.screens.images.ImagesAdapter.ImageViewHolder
import kotlinx.android.synthetic.main.item_image.view.image

class ImagesAdapter(
        private val context: Context
) : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = (0..100).map { Unit }.toMutableList()

    class ImageViewHolder(
            view: View
    ) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.image
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ImageViewHolder {
        val li = LayoutInflater.from(context)
        val view = li.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(
            holder: ImageViewHolder,
            position: Int
    ) {
        holder.image.setImageResource(R.mipmap.ic_launcher)
    }
}