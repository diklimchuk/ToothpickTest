package com.github.toothpicktest.presentation.screens.images

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.toothpicktest.R
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.glide.load
import com.github.toothpicktest.presentation.screens.images.ImagesAdapter.ImageViewHolder
import kotlinx.android.synthetic.main.item_image.view.image
import kotlinx.android.synthetic.main.item_image.view.tags

class ImagesAdapter(
        private val activity: Activity
) : RecyclerView.Adapter<ImageViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val images = arrayListOf<Image>()

    class ImageViewHolder(
            view: View
    ) : RecyclerView.ViewHolder(view) {
        val tags: TextView = view.tags
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

    override fun getItemId(position: Int): Long = images[position].id

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(
            holder: ImageViewHolder,
            position: Int
    ) {
        val image = images[position]
        holder.image.load(activity, image.url)
        holder.tags.text = image.tags.joinToString(" ")
    }

    /**
     * @param position Adapter position of the item to return
     */
    fun getImage(position: Int) = images[position]

    fun clear() {
        val previousSize = images.size
        images.clear()
        notifyItemRangeRemoved(0, previousSize)
    }

    fun addImages(images: Collection<Image>) {
        val previousSize = this.images.size
        this.images.addAll(images)
        notifyItemRangeInserted(previousSize, images.size)
    }
}