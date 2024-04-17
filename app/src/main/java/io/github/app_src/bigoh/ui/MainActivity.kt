package io.github.app_src.bigoh.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.app_src.bigoh.R
import io.github.app_src.bigoh.adapter.ImageListAdapter
import io.github.app_src.bigoh.model.ImageItem
import io.github.app_src.bigoh.repository.ImageRepository
import io.github.app_src.bigoh.repository.ImageViewModelFactory
import io.github.app_src.bigoh.viewmodel.ImageViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ImageViewModel
    private lateinit var adapter: ImageListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageRepository = ImageRepository()
        val viewModelFactory = ImageViewModelFactory(imageRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageViewModel::class.java)
        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        val itemClickListener: (ImageItem) -> Unit = { imageItem ->
            val intent = Intent(this, ImageViewActivity::class.java).apply {
                putExtra("ImageItem", imageItem)
            }
            startActivity(intent)
        }

        adapter = ImageListAdapter(listOf(), itemClickListener)
        layoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        viewModel.images.observe(this, Observer {
            adapter.updateData(it)
        })

        viewModel.refreshImages()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!viewModel.isLastPage && totalItemCount <= (lastVisibleItem + 5)) {
                    viewModel.loadImages()
                }
            }
        })

    }
}
