package com.example.tokobuku

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "imageRes")
    val imageRes: Int,

    @ColumnInfo(name = "rating")
    val rating: Float,

    @ColumnInfo(name = "reviews")
    val reviews: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "tags")
    val tags: Array<String>,

    @ColumnInfo(name = "backgroundColorResId")
    val backgroundColorResId: Int
) : Parcelable {
    @Ignore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false
        if (title != other.title) return false
        if (author != other.author) return false
        if (imageRes != other.imageRes) return false
        if (rating != other.rating) return false
        if (reviews != other.reviews) return false
        if (description != other.description) return false
        if (!tags.contentEquals(other.tags)) return false
        if (backgroundColorResId != other.backgroundColorResId) return false

        return true
    }

    @Ignore
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + imageRes
        result = 31 * result + rating.hashCode()
        result = 31 * result + reviews
        result = 31 * result + description.hashCode()
        result = 31 * result + tags.contentHashCode()
        result = 31 * result + backgroundColorResId
        return result
    }
}
