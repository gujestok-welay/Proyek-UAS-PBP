package com.example.tokobuku

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tokobuku.model.UserRole
import java.util.*

class BookAdapter(private var bookList: MutableList<Book>, private val userRole: UserRole?) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>(), Filterable {

    private var bookListFull: List<Book> = ArrayList(bookList)

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.bookCoverImageView)
        val bookTitle: TextView = itemView.findViewById(R.id.bookTitleTextView)
        val bookAuthor: TextView = itemView.findViewById(R.id.bookAuthorTextView)
        val bookRating: TextView = itemView.findViewById(R.id.bookRatingTextView)
        val bookReviews: TextView = itemView.findViewById(R.id.bookReviewsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList[position]

        if (currentBook.imageUri != null) {
            holder.bookImage.setImageURI(Uri.parse(currentBook.imageUri))
        } else {
            holder.bookImage.setImageResource(currentBook.imageRes)
        }
        holder.bookTitle.text = currentBook.title
        holder.bookAuthor.text = currentBook.author
        holder.bookRating.text = currentBook.rating.toString()
        holder.bookReviews.text = "(${currentBook.reviews} Reviews)"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BookDetailActivity::class.java).apply {
                putExtra("BOOK_ID", currentBook.id)
                putExtra(BookListActivity.EXTRA_USER_ROLE, userRole)
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
