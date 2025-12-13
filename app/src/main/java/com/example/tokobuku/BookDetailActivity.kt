package com.example.tokobuku

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log // 1. PASTIKAN IMPORT LOG SUDAH ADA
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.math.floor

class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        // Inisialisasi Views
        val appBarLayout: AppBarLayout = findViewById(R.id.appBarLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val bookCoverImageView: ImageView = findViewById(R.id.bookCoverImageView)
        val bookTitleTextView: TextView = findViewById(R.id.bookTitleTextView)
        val bookAuthorTextView: TextView = findViewById(R.id.bookAuthorTextView)
        val bookDescriptionTextView: TextView = findViewById(R.id.bookDescriptionTextView)
        val tagChipGroup: ChipGroup = findViewById(R.id.tagChipGroup)
        val ratingStarsLayout: LinearLayout = findViewById(R.id.ratingStarsLayout)

        // Setup Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Ambil data yang dikirim (HANYA SEKALI DI ATAS)
        val title = intent.getStringExtra("BOOK_TITLE")
        val author = intent.getStringExtra("BOOK_AUTHOR")
        val description = intent.getStringExtra("BOOK_DESCRIPTION")
        val imageResId = intent.getIntExtra("BOOK_IMAGE_RES", 0)
        val tags = intent.getStringArrayExtra("BOOK_TAGS")
        val backgroundColorResId = intent.getIntExtra("BOOK_BACKGROUND_RES", 0)
        val rating = intent.getFloatExtra("BOOK_RATING", 0.0f) // <-- Variabel rating sudah diambil di sini

        // Set data ke Views
        bookTitleTextView.text = title
        bookAuthorTextView.text = author
        bookDescriptionTextView.text = description
        if (imageResId != 0) {
            bookCoverImageView.setImageResource(imageResId)
        }

        // Atur background AppBarLayout
        if (backgroundColorResId != 0) {
            val color = ContextCompat.getColor(this, backgroundColorResId)
            appBarLayout.setBackgroundColor(color)
        } else {
            val defaultColor = ContextCompat.getColor(this, R.color.bg_book_default)
            appBarLayout.setBackgroundColor(defaultColor)
        }

        // --- BAGIAN YANG DIPERBAIKI ---
        // 2. Gunakan Log.d di baris terpisah
        Log.d("BookDetail", "Rating yang diterima: $rating")

        // 3. Tampilkan rating menggunakan variabel yang sudah ada
        displayRating(rating, ratingStarsLayout)
        // --- AKHIR BAGIAN PERBAIKAN ---

        // Logika untuk Chip
        val colorPairs = listOf(
            Pair(R.color.chip_bg_blue, R.color.chip_text_blue),
            Pair(R.color.chip_bg_green, R.color.chip_text_green),
            Pair(R.color.chip_bg_orange, R.color.chip_text_orange)
        )
        tags?.forEachIndexed { index, tagName ->
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
    }



    // --- FUNGSI YANG SUDAH DIPERBAIKI UNTUK TAMPILAN KONSISTEN ---
    // --- FUNGSI FINAL DENGAN LOGIKA BINTANG SETENGAH ---
    // --- FUNGSI INI SEKARANG AKAN BEKERJA DENGAN BENAR ---
    private fun displayRating(rating: Float, layout: LinearLayout) {
        layout.removeAllViews()

        // Mengambil warna dari colors.xml, misalnya #FFC107
        val starColor = ContextCompat.getColor(this, R.color.star_color)

        // Loop sebanyak 5 kali untuk 5 bintang
        for (i in 1..5) {
            val star = ImageView(this)

            // Menggunakan 'when' untuk logika yang lebih jelas
            when {
                // Kondisi 1: Bintang penuh
                i <= rating -> {
                    star.setImageResource(R.drawable.ic_star_filled) // Gunakan ikon Anda
                }

                // Kondisi 2: Bintang setengah
                i - 1 < rating && i > rating -> {
                    star.setImageResource(R.drawable.ic_star_half) // Gunakan ikon Anda
                }

                // Kondisi 3: Bintang kosong
                else -> {
                    star.setImageResource(R.drawable.ic_star_border) // Gunakan ikon Anda
                }
            }

            // Atur warna kuning untuk semua jenis bintang
            // Ini akan menimpa warna fillColor di dalam file XML, memberikan kontrol terpusat
            star.setColorFilter(starColor)
            layout.addView(star)
        }
    }





}
