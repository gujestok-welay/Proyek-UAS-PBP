package com.example.tokobuku

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.tokobuku.activity.AddBookActivity
import com.example.tokobuku.model.UserRole
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import kotlin.math.floor

class BookDetailActivity : AppCompatActivity() {

    private lateinit var db: BookDatabase
    private var userRole: UserRole? = null
    private var currentBook: Book? = null
    private var bookId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        db = BookDatabase.getDatabase(this)

        userRole = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(BookListActivity.EXTRA_USER_ROLE, UserRole::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(BookListActivity.EXTRA_USER_ROLE) as? UserRole
        }

        bookId = intent.getIntExtra("BOOK_ID", -1)

        if (bookId == -1) {
            // Handle error: No book ID provided
            finish()
            return
        }

        val appBarLayout: AppBarLayout = findViewById(R.id.appBarLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val bookCoverImageView: ImageView = findViewById(R.id.bookCoverImageView)
        val bookTitleTextView: TextView = findViewById(R.id.bookTitleTextView)
        val bookAuthorTextView: TextView = findViewById(R.id.bookAuthorTextView)
        val bookDescriptionTextView: TextView = findViewById(R.id.bookDescriptionTextView)
        val tagChipGroup: ChipGroup = findViewById(R.id.tagChipGroup)
        val ratingStarsLayout: LinearLayout = findViewById(R.id.ratingStarsLayout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        db.bookDao().getBookById(bookId).observe(this) { book ->
            if (book == null) {
                // The book was deleted, so finish this activity
                finish()
                return@observe
            }
            currentBook = book
            bookTitleTextView.text = book.title
            bookAuthorTextView.text = book.author
            bookDescriptionTextView.text = book.description

            if (book.imageUri != null) {
                bookCoverImageView.setImageURI(Uri.parse(book.imageUri))
            } else if (book.imageRes != 0) {
                bookCoverImageView.setImageResource(book.imageRes)
            }

            val color = ContextCompat.getColor(this, book.backgroundColorResId)
            appBarLayout.setBackgroundColor(color)

            displayRating(book.rating, ratingStarsLayout)
            tagChipGroup.removeAllViews() // Clear old tags before adding new ones

            val colorPairs = listOf(
                Pair(R.color.chip_bg_blue, R.color.chip_text_blue),
                Pair(R.color.chip_bg_green, R.color.chip_text_green),
                Pair(R.color.chip_bg_orange, R.color.chip_text_orange)
            )

            book.tags.forEachIndexed { index, tagName ->
                val chip = Chip(this)
                chip.text = tagName
                chip.isCloseIconVisible = false
                val colorPair = colorPairs[index % colorPairs.size]
                val chipBackgroundColor = ContextCompat.getColor(this, colorPair.first)
                val chipTextColor = ContextCompat.getColor(this, colorPair.second)
                chip.chipBackgroundColor = ColorStateList.valueOf(chipBackgroundColor)
                chip.setTextColor(chipTextColor)
                tagChipGroup.addView(chip)
            }

            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (userRole is UserRole.Admin) {
            menuInflater.inflate(R.menu.detail_menu_admin, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, AddBookActivity::class.java).apply {
                    putExtra("BOOK_EXTRA", currentBook)
                }
                startActivity(intent)
                true
            }
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Book")
            .setMessage("Are you sure you want to delete this book?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    currentBook?.let { db.bookDao().delete(it) }
                    // The observer will handle finishing the activity
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun displayRating(rating: Float, layout: LinearLayout) {
        layout.removeAllViews()
        val starColor = ContextCompat.getColor(this, R.color.star_color)
        for (i in 1..5) {
            val star = ImageView(this)
            when {
                i <= rating -> star.setImageResource(R.drawable.ic_star_filled)
                i - 1 < rating && i > rating -> star.setImageResource(R.drawable.ic_star_half)
                else -> star.setImageResource(R.drawable.ic_star_border)
            }
            star.setColorFilter(starColor)
            layout.addView(star)
        }
    }
}
