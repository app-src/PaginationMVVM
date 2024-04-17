package io.github.app_src.bigoh.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.app_src.bigoh.model.ImageItem
import io.github.app_src.bigoh.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ImageViewModel(private val imageRepository: ImageRepository) : ViewModel() {
    private var tempList: List<ImageItem> = listOf()
    private val _images = MutableLiveData<List<ImageItem>>()
    val images: LiveData<List<ImageItem>> = _images

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 1
    var isLastPage = false

    init{
        loadImages()
    }

    fun loadImages() {
        if (isLastPage || _isLoading.value == true) {
            return
        }

        _isLoading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            try {
                tempList = imageRepository.fetchImages(currentPage, 30).toList() // Assuming 30 items per page

            } catch (e: Exception) {
                // Handle the error appropriately
                Log.e("ImageViewModel", "Failed to fetch images: ${e.message}")
            }
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                isLastPage = tempList.size < 30
                currentPage++
                _images.postValue(tempList)
            }
        }
    }


    fun refreshImages() {
        currentPage = 1
        isLastPage = false
        _images.value = emptyList()
        loadImages()
    }
}


