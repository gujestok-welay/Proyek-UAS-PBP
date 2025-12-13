package com.example.tokobuku

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Tambahkan anotasi @Parcelize dan implementasi Parcelable jika ingin mengirim objek Book secara langsung
// Namun untuk sekarang, kita kirim propertinya satu per satu agar lebih sederhana.

data class Book(
    val title: String,
    val author: String,
    val imageRes: Int, // Ubah dari 'cover' agar konsisten
    val rating: Float,
    val reviews: Int,
    val description: String, // Properti yang dibutuhkan
    val tags: Array<String>,  // Properti yang dibutuhkan
    val backgroundColorResId: Int
) {
    // Dibutuhkan untuk perbandingan Array
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (title != other.title) return false
        if (author != other.author) return false
        if (imageRes != other.imageRes) return false
        if (rating != other.rating) return false
        if (reviews != other.reviews) return false
        if (description != other.description) return false
        if (!tags.contentEquals(other.tags)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + imageRes
        result = 31 * result + rating.hashCode()
        result = 31 * result + reviews
        result = 31 * result + description.hashCode()
        result = 31 * result + tags.contentHashCode()
        return result
    }
}
