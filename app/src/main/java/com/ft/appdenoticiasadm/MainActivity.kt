package com.ft.appdenoticiasadm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ft.appdenoticiasadm.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnPublishNews.setOnClickListener {
            val title = binding.editNewsTitle.text.toString()
            val description = binding.editNewsDescription.text.toString()
            val date = binding.editPublishDate.text.toString()
            val author = binding.editAuthor.text.toString()

            if (title.isEmpty() || description.isEmpty() || date.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_missing_fields),
                    Toast.LENGTH_SHORT).show()
            } else {
                saveNews(title, description, date, author)
            }
        }
    }

    private fun saveNews(title: String, description: String, date: String, author: String) {
        val mapNews = hashMapOf(
            "title" to title,
            "description" to description,
            "date" to date,
            "author" to author
        )
        db.collection("news").document("aNews")
            .set(mapNews).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.info_news_published),
                        Toast.LENGTH_SHORT).show()
                    cleanFields()
                }
            }.addOnFailureListener {

            }
    }

    private fun cleanFields() {
        binding.editNewsTitle.setText("")
        binding.editNewsDescription.setText("")
        binding.editPublishDate.setText("")
        binding.editAuthor.setText("")
    }
}