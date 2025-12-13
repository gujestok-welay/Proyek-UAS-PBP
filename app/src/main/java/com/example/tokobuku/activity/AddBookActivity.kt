package com.example.tokobuku.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tokobuku.databinding.ActivityAddBookBinding

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadImageButton.setOnClickListener {
            Toast.makeText(this, "Image upload feature will be implemented later.", Toast.LENGTH_SHORT).show()
        }

        binding.saveBookButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val author = binding.authorEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val price = binding.priceEditText.text.toString()
            val stock = binding.stockEditText.text.toString()

            if (title.isNotEmpty() && author.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && stock.isNotEmpty()) {
                // TODO: Implement actual database logic
                Toast.makeText(this, "Book Saved! (Simulation)", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
