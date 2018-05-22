package com.github.toothpicktest.presentation.screens.images

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.toothpicktest.R
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.glide.load
import com.github.toothpicktest.presentation.screens.images.ImagesAdapter.ImageViewHolder
import kotlinx.android.synthetic.main.item_image.view.image

class ImagesAdapter(
        private val activity: Activity
) : RecyclerView.Adapter<ImageViewHolder>() {

    private val images = mutableListOf<Image>()

    class ImageViewHolder(
            view: View
    ) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.image
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ImageViewHolder {
        val li = LayoutInflater.from(activity)
        val view = li.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(
            holder: ImageViewHolder,
            position: Int
    ) {
        val image = images[position]
        holder.image.load(activity, image.url)
    }

    fun replaceImages(images: Collection<Image>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }
}