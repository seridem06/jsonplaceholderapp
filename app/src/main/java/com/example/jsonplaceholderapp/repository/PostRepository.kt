package com.example.jsonplaceholderapp.repository

import android.util.Log
import com.example.jsonplaceholderapp.model.Post
import com.example.jsonplaceholderapp.network.RetrofitClient

class PostRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getAllPosts(): List<Post>? {
        return try {
            Log.d("PostRepository", "🔄 Iniciando llamada a la API...")
            val response = apiService.getAllPosts()
            Log.d("PostRepository", "📡 Código de respuesta: ${response.code()}")

            if (response.isSuccessful) {
                val posts = response.body()
                Log.d("PostRepository", "✅ Posts obtenidos: ${posts?.size}")
                posts
            } else {
                Log.e("PostRepository", "❌ Error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "💥 Excepción: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}