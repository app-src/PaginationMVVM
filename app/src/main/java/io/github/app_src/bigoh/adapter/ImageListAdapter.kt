package io.github.app_src.bigoh.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.app_src.bigoh.R
import io.github.app_src.bigoh.model.ImageItem
import java.net.URL

class ImageListAdapter(
    private var items: List<ImageItem>,
    private val itemClickListener: (ImageItem) -> Unit
) : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ImageViewHolder(
        itemView: View,
        private val itemClickListener: (ImageItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)

        fun bind(imageItem: ImageItem) {
            Thread {
                try {
                    val url = URL(imageItem.downloadUrl)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    imageView.post {
                        imageView.setImageBitmap(bmp)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("ImageListAdapter", "Failed to load image: ${imageItem.downloadUrl}")
                    // Handle the error appropriately
                }
            }.start()
            authorTextView.text = imageItem.author

            itemView.setOnClickListener {
                itemClickListener(imageItem)
            }
        }
    }

    // Update the data and notify the adapter
    fun updateData(newItems: List<ImageItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}