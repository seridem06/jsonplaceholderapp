package com.example.jsonplaceholderapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var etId: EditText
    private lateinit var etUserId: EditText
    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText
    private lateinit var btnBack: Button
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
        loadPostData()
        setupListeners()
    }

    private fun initViews() {
        etId = findViewById(R.id.etId)
        etUserId = findViewById(R.id.etUserId)
        etTitle = findViewById(R.id.etTitle)
        etBody = findViewById(R.id.etBody)
        btnBack = findViewById(R.id.btnBack)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun loadPostData() {
        // Obtener datos del Intent
        val postId = intent.getIntExtra("POST_ID", 0)
        val postUserId = intent.getIntExtra("POST_USER_ID", 0)
        val postTitle = intent.getStringExtra("POST_TITLE") ?: ""
        val postBody = intent.getStringExtra("POST_BODY") ?: ""

        // Mostrar datos
        etId.setText(postId.toString())
        etUserId.setText(postUserId.toString())
        etTitle.setText(postTitle)
        etBody.setText(postBody)
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish() // Volver a MainActivity
        }

        btnSave.setOnClickListener {
            savePost()
        }

        btnDelete.setOnClickListener {
            deletePost()
        }
    }

    private fun savePost() {
        val id = etId.text.toString()
        val userId = etUserId.text.toString()
        val title = etTitle.text.toString()
        val body = etBody.text.toString()

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí iría la lógica para actualizar el post en la API
        Toast.makeText(this, "Post $id guardado exitosamente", Toast.LENGTH_SHORT).show()
    }

    private fun deletePost() {
        val id = etId.text.toString()

        // Aquí iría la lógica para eliminar el post en la API
        Toast.makeText(this, "Post $id eliminado", Toast.LENGTH_SHORT).show()

        // Volver a la lista
        finish()
    }
}