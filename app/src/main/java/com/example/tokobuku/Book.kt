package com.example.tokobuku

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
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

    @ColumnInfo(name = "imageUri")
    val imageUri: String? = null,

    @ColumnInfo(name = "rating")
    val rating: Float,

    @ColumnInfo(name = "reviews")
    val reviews: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "tags")
    val tags: List<String>,

    @ColumnInfo(name = "backgroundColorResId")
    val backgroundColorResId: Int
) : Parcelable
