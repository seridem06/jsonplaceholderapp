package com.example.jsonplaceholderapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonplaceholderapp.adapter.PostAdapter
import com.example.jsonplaceholderapp.model.Post
import com.example.jsonplaceholderapp.viewmodel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PostViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: ImageButton
    private lateinit var fabAddPost: FloatingActionButton
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "🎬 Iniciando aplicación")

        initViews()
        setupRecyclerView()
        setupViewModel()
        setupListeners()

        // Mostrar mensaje de carga
        Toast.makeText(this, "Cargando posts desde API...", Toast.LENGTH_SHORT).show()

        // Cargar posts
        viewModel.fetchAllPosts()
    }

    override fun onResume() {
        super.onResume()
        // Recargar posts al volver de DetailActivity
        Log.d("MainActivity", "🔄 Recargando posts...")
        viewModel.fetchAllPosts()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
        fabAddPost = findViewById(R.id.fabAddPost)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(emptyList()) { post ->
            Log.d("MainActivity", "👆 Click en post: ${post.id}")
            openDetailActivity(post)
        }
        recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        viewModel.posts.observe(this) { posts ->
            Log.d("MainActivity", "📋 ${posts.size} posts recibidos")

            if (posts.isEmpty()) {
                tvEmpty.text = "No hay posts disponibles"
                tvEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                adapter.updatePosts(posts)
                recyclerView.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
                Toast.makeText(this, "${posts.size} posts cargados", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            Log.d("MainActivity", "⏳ Loading: $isLoading")
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

            if (isLoading) {
                recyclerView.visibility = View.GONE
                tvEmpty.visibility = View.GONE
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            Log.e("MainActivity", "❌ Error: $errorMessage")
            tvEmpty.text = "Error: $errorMessage\n\nVerifica tu conexión a Internet"
            tvEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListeners() {
        // Búsqueda en tiempo real
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnSearch.setOnClickListener {
            adapter.filter(etSearch.text.toString())
        }

        // Botón flotante para crear nuevo post
        fabAddPost.setOnClickListener {
            createNewPost()
        }
    }

    private fun openDetailActivity(post: Post) {
        Log.d("MainActivity", "🚀 Abriendo detalles del post ${post.id}")
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("POST_ID", post.id)
            putExtra("POST_USER_ID", post.userId)
            putExtra("POST_TITLE", post.title)
            putExtra("POST_BODY", post.body)
        }
        startActivity(intent)
    }

    private fun createNewPost() {
        Log.d("MainActivity", "➕ Creando nuevo post")
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("POST_ID", 0) // Nuevo post
            putExtra("POST_USER_ID", 1)
            putExtra("POST_TITLE", "")
            putExtra("POST_BODY", "")
        }
        startActivity(intent)
    }
}
