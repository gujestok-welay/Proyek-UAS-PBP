package com.example.tokobuku

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class BookAdapter(private var bookList: MutableList<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>(), Filterable {

    private var bookListFull: List<Book> = ArrayList(bookList)

    // 1. PERBARUI VIEWHOLDER: Tambahkan referensi untuk rating dan review
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.bookCoverImageView)
        val bookTitle: TextView = itemView.findViewById(R.id.bookTitleTextView)
        val bookAuthor: TextView = itemView.findViewById(R.id.bookAuthorTextView)
        val bookRating: TextView = itemView.findViewById(R.id.bookRatingTextView) // BARU
        val bookReviews: TextView = itemView.findViewById(R.id.bookReviewsTextView) // BARU
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        // Ganti R.layout.list_item_book dengan yang baru
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_book, parent, false)
        return BookViewHolder(view)
    }

    // 2. PERBARUI ONBINDVIEWHOLDER: Isi data rating dan review
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList[position]
        holder.bookImage.setImageResource(currentBook.imageRes)
        holder.bookTitle.text = currentBook.title
        holder.bookAuthor.text = currentBook.author

        // --- MENGISI DATA BARU ---
        holder.bookRating.text = currentBook.rating.toString()
        holder.bookReviews.text = "(${currentBook.reviews} Reviews)" // Format teksnya

        // Listener klik tidak perlu diubah, sudah benar
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BookDetailActivity::class.java).apply {
                putExtra("BOOK_TITLE", currentBook.title)
                putExtra("BOOK_AUTHOR", currentBook.author)
                putExtra("BOOK_IMAGE_RES", currentBook.imageRes)
                putExtra("BOOK_DESCRIPTION", currentBook.description)
                putExtra("BOOK_TAGS", currentBook.tags)
                putExtra("BOOK_BACKGROUND_RES", currentBook.backgroundColorResId)
                putExtra("BOOK_RATING", currentBook.rating)
                putExtra("BOOK_REVIEWS", currentBook.reviews) // Kirim juga jumlah review
            }
            context.startActivity(intent)
        }
    }

    fun updateData(newBooks: List<Book>) {
        bookList.clear()
        bookList.addAll(newBooks)
        bookListFull = ArrayList(newBooks)
        notifyDataSetChanged()
    }

    override fun getItemCount() = bookList.size

    fun updateFullList() {
        bookListFull = ArrayList(bookList)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: MutableList<Book> = ArrayList()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(bookListFull)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    for (item in bookListFull) {
                        if (item.title.lowercase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                bookList.clear()
                if (results?.values != null) {
                    bookList.addAll(results.values as List<Book>)
                }
                notifyDataSetChanged()
            }
        }
    }
}
