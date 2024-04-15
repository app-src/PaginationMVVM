package io.github.app_src.bigoh.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.app_src.bigoh.viewmodel.ImageViewModel

class ImageViewModelFactory(private val imageRepository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            return ImageViewModel(imageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

