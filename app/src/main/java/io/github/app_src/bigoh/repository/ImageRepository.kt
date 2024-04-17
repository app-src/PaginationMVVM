package io.github.app_src.bigoh.repository

import android.util.Log
import io.github.app_src.bigoh.model.ImageItem
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ImageRepository {

    fun fetchImages(page: Int, limit: Int): List<ImageItem> {
        val urlString = "https://picsum.photos/v2/list?page=$page&limit=$limit"

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        val response = StringBuilder()

        Log.e("ImageRepository", "Response code: $responseCode")

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                var inputLine = reader.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = reader.readLine()
                }
            }

            val imageList = mutableListOf<ImageItem>()
            val jsonArray = JSONArray(response.toString())

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id").toInt()
                val author = jsonObject.getString("author")
                val width =  jsonObject.getString("width").toInt()
                val height = jsonObject.getString("height").toInt()
                val url = jsonObject.getString("url").toString()
                val downloadUrl = jsonObject.getString("download_url").toString()
                imageList.add(ImageItem(id, author, width, height, url, downloadUrl))
            }

            return imageList
        } else {
            // Handle the error appropriately
            throw Exception("Failed to fetch images: $responseCode")
        }

    }

}