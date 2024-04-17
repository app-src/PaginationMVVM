package io.github.app_src.bigoh.ui

import android.graphics.BitmapFactory
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.github.app_src.bigoh.R
import io.github.app_src.bigoh.databinding.ActivityImageViewBinding
import io.github.app_src.bigoh.model.ImageItem
import java.net.URL

class ImageViewActivity : AppCompatActivity() {
    private lateinit var imageItem: ImageItem
    private lateinit var binding: ActivityImageViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                imageItem = intent.getSerializableExtra("ImageItem",ImageItem::class.java)!!
            }
            else -> {
                @Suppress("DEPRECATION")
                imageItem = intent.getSerializableExtra("ImageItem") as ImageItem
            }
        }
        if (imageItem != null) {
            binding.authorTextView.text = "Author: ${imageItem.author}"
            binding.widthTextView.text = "Image Width: ${imageItem.width}"
            binding.heightTextView.text = "Image Height: ${imageItem.height}"
            binding.idTextView.text = "Image ID: ${imageItem.id}"
            Thread {
                try {
                    val url = URL(imageItem.downloadUrl)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    binding.imageView.post {
                        binding.imageView.setImageBitmap(bmp)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("ImageListAdapter", "Failed to load image: ${imageItem.downloadUrl}")
                }
            }.start()

        }

        binding.backBtnImageView.setOnClickListener {
            finish()
        }

    }
}