package com.example.tokobuku.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.tokobuku.Book
import com.example.tokobuku.BookDatabase
import com.example.tokobuku.R
import com.example.tokobuku.databinding.ActivityAddBookBinding
import kotlinx.coroutines.launch

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding
    private lateinit var db: BookDatabase
    private var currentBook: Book? = null
    private var selectedImageUri: String? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = it.toString()
            binding.bookCoverImageView.setImageURI(it)
            Toast.makeText(this, "Image selected successfully!", Toast.LENGTH_SHORT).show()
            binding.uploadImageButton.text = "Change Image"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BookDatabase.getDatabase(this)

        currentBook = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("BOOK_EXTRA", Book::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("BOOK_EXTRA") as? Book
        }

        currentBook?.let {
            binding.titleEditText.setText(it.title)
            binding.authorEditText.setText(it.author)
            binding.descriptionEditText.setText(it.description)
            binding.tagsEditText.setText(it.tags.joinToString(", "))
            it.imageUri?.let {
                uriString -> binding.bookCoverImageView.setImageURI(Uri.parse(uriString))
                binding.uploadImageButton.text = "Change Image"
            }
        }

        binding.uploadImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.saveBookButton.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook() {
        val title = binding.titleEditText.text.toString().trim()
        val author = binding.authorEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        // Convert comma-separated string to a List<String> for the database
        val tags = binding.tagsEditText.text.toString().split(",").map { it.trim() }

        if (title.isNotEmpty() && author.isNotEmpty() && description.isNotEmpty()) {
            lifecycleScope.launch {
                if (currentBook == null) {
                    val newBook = Book(
                        title = title,
                        author = author,
                        description = description,
                        imageRes = R.drawable.gradient_background_placeholder, // Default placeholder image
                        imageUri = selectedImageUri,
                        rating = 0f, // Default rating
                        reviews = 0, // Default reviews
                        tags = tags, // Default tag
                        backgroundColorResId = R.color.bg_book_default // Default background
                    )
                    db.bookDao().insert(newBook)
                    Toast.makeText(this@AddBookActivity, "Book saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val updatedBook = currentBook!!.copy(
                        title = title,
                        author = author,
                        description = description,
                        tags = tags,
                        imageUri = selectedImageUri ?: currentBook!!.imageUri
                    )
                    db.bookDao().update(updatedBook)
                    Toast.makeText(this@AddBookActivity, "Book updated successfully", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        } else {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
        }
    }
}
