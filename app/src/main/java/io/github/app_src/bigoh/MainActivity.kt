package io.github.app_src.bigoh


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            // Handle the click event, e.g., start a new activity with the image details
            Toast.makeText(this, "Clicked on ${imageItem.author}", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ImageViewActivity::class.java).apply {
//                putExtra("EXTRA_IMAGE_URL", imageItem.downloadUrl)
//                putExtra("EXTRA_AUTHOR_NAME", imageItem.author)
//            }
//            startActivity(intent)
        }

        adapter = ImageListAdapter(listOf(ImageItem(1,"Ashish",400,500,"https://unsplash.com/photos/yC-Yzbqy7PY","https://picsum.photos/id/0/5000/3333"),ImageItem(1,"Ashish",400,500,"https://unsplash.com/photos/yC-Yzbqy7PY","https://picsum.photos/id/0/5000/3333")),itemClickListener)
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
