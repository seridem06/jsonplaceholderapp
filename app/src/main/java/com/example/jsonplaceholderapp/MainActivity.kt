package com.example.jsonplaceholderapp

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

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PostViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: PostAdapter

    // Campos del formulario
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: ImageButton
    private lateinit var etId: EditText
    private lateinit var etUserId: EditText
    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var selectedPost: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "🎬 onCreate iniciado")

        initViews()
        setupRecyclerView()
        setupViewModel()
        setupListeners()

        // Cargar datos desde la API
        Log.d("MainActivity", "📥 Solicitando posts...")
        viewModel.fetchAllPosts()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
        etId = findViewById(R.id.etId)
        etUserId = findViewById(R.id.etUserId)
        etTitle = findViewById(R.id.etTitle)
        etBody = findViewById(R.id.etBody)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        Log.d("MainActivity", "✅ Vistas inicializadas")
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(emptyList()) { post ->
            onPostSelected(post)
        }
        recyclerView.adapter = adapter
        Log.d("MainActivity", "✅ RecyclerView configurado")
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        viewModel.posts.observe(this) { posts ->
            Log.d("MainActivity", "📋 Posts recibidos en UI: ${posts.size}")
            adapter.updatePosts(posts)
            recyclerView.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE

            // Seleccionar automáticamente el primer post
            if (posts.isNotEmpty()) {
                onPostSelected(posts[0])
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            Log.d("MainActivity", "⏳ Loading: $isLoading")
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(this) { errorMessage ->
            Log.e("MainActivity", "❌ Error: $errorMessage")
            tvEmpty.text = errorMessage
            tvEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        Log.d("MainActivity", "✅ ViewModel configurado")
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

        // Botones
        btnSave.setOnClickListener {
            savePost()
        }

        btnDelete.setOnClickListener {
            deletePost()
        }
    }

    private fun onPostSelected(post: Post) {
        Log.d("MainActivity", "🎯 Post seleccionado: ${post.id}")
        selectedPost = post
        etId.setText(post.id.toString())
        etUserId.setText(post.userId.toString())
        etTitle.setText(post.title)
        etBody.setText(post.body)

        Toast.makeText(this, "Post ${post.id} seleccionado", Toast.LENGTH_SHORT).show()
    }

    private fun savePost() {
        if (selectedPost == null) {
            Toast.makeText(this, "Selecciona un post primero", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedPost = Post(
            id = etId.text.toString().toIntOrNull() ?: 0,
            userId = etUserId.text.toString().toIntOrNull() ?: 0,
            title = etTitle.text.toString(),
            body = etBody.text.toString()
        )

        Log.d("MainActivity", "💾 Guardando post: ${updatedPost.id}")
        Toast.makeText(this, "Post ${updatedPost.id} guardado (simulado)", Toast.LENGTH_SHORT).show()
    }

    private fun deletePost() {
        if (selectedPost == null) {
            Toast.makeText(this, "Selecciona un post primero", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "🗑️ Eliminando post: ${selectedPost?.id}")
        Toast.makeText(this, "Post ${selectedPost?.id} eliminado (simulado)", Toast.LENGTH_SHORT).show()
        clearForm()
    }

    private fun clearForm() {
        selectedPost = null
        etId.setText("")
        etUserId.setText("")
        etTitle.setText("")
        etBody.setText("")
    }
}