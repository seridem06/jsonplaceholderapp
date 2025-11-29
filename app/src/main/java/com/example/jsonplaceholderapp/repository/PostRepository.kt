package com.example.jsonplaceholderapp.repository

import android.util.Log
import com.example.jsonplaceholderapp.model.Post
import com.example.jsonplaceholderapp.network.RetrofitClient
import kotlinx.coroutines.delay

class PostRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getAllPosts(): List<Post>? {
        return try {
            Log.d("PostRepository", "🔄 Intentando conectar con API...")
            val response = apiService.getAllPosts()
            Log.d("PostRepository", "📡 Código: ${response.code()}")

            if (response.isSuccessful) {
                val posts = response.body()
                Log.d("PostRepository", "✅ ${posts?.size} posts desde API")
                posts
            } else {
                Log.e("PostRepository", "❌ Error API: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "💥 Error de conexión: ${e.message}")

            // DATOS DE PRUEBA - Solo si falla la API
            Log.d("PostRepository", "⚠️ Usando datos de prueba")
            delay(500)
            return listOf(
                Post(1, 1, "Post de Prueba 1", "Contenido del primer post de prueba. La API no está disponible."),
                Post(2, 1, "Post de Prueba 2", "Contenido del segundo post de prueba. Verifica tu conexión."),
                Post(3, 2, "Post de Prueba 3", "Contenido del tercer post de prueba."),
                Post(4, 2, "Post de Prueba 4", "Contenido del cuarto post de prueba."),
                Post(5, 3, "Post de Prueba 5", "Contenido del quinto post de prueba.")
            )
        }
    }
}