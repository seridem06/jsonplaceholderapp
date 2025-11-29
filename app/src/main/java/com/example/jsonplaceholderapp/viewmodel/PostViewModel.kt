package com.example.jsonplaceholderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonplaceholderapp.model.Post
import com.example.jsonplaceholderapp.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchAllPosts() {
        Log.d("PostViewModel", "🚀 fetchAllPosts iniciado")
        _loading.value = true

        viewModelScope.launch {
            try {
                Log.d("PostViewModel", "📞 Llamando al repositorio...")
                val postList = repository.getAllPosts()

                if (postList != null && postList.isNotEmpty()) {
                    Log.d("PostViewModel", "✅ ${postList.size} posts recibidos")
                    _posts.value = postList
                    _loading.value = false
                } else {
                    Log.e("PostViewModel", "⚠️ Lista vacía o nula")
                    _error.value = "No se pudieron cargar los posts"
                    _loading.value = false
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "💥 Error: ${e.message}")
                e.printStackTrace()
                _error.value = "Error de conexión: ${e.message}"
                _loading.value = false
            }
        }
    }
}