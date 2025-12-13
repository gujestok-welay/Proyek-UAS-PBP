package com.example.tokobuku

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tokobuku.activity.AddBookActivity
import com.example.tokobuku.databinding.ActivityBookListBinding
import com.example.tokobuku.model.UserRole
import kotlinx.coroutines.launch

class BookListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookListBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var db: BookDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BookDatabase.getDatabase(this)

        val userRole = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_USER_ROLE, UserRole::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER_ROLE) as? UserRole
        }

        when (userRole) {
            is UserRole.Admin -> {
                Toast.makeText(this, "Login successful. Role: Admin", Toast.LENGTH_SHORT).show()
                binding.addBookFab.visibility = View.VISIBLE
                binding.addBookFab.setOnClickListener {
                    startActivity(Intent(this, AddBookActivity::class.java))
                }
            }
            is UserRole.User, null -> {
                if (userRole is UserRole.User) {
                    Toast.makeText(this, "Login successful. Role: User", Toast.LENGTH_SHORT).show()
                }
                binding.addBookFab.visibility = View.GONE
            }
        }

        bookAdapter = BookAdapter(mutableListOf())
        binding.booksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.booksRecyclerView.adapter = bookAdapter

        db.bookDao().getAllBooks().observe(this) { books ->
            if (books.isEmpty()) {
                lifecycleScope.launch {
                    insertInitialData()
                }
            } else {
                bookAdapter.updateData(books)
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bookAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private suspend fun insertInitialData() {
        val initialBooks = mutableListOf(
            Book(
                title = "Makanya, Mikir!",
                author = "Abigail Limuria & Cania Citta",
                imageRes = R.drawable.book_makanya_mikir,
                rating = 5f,
                reviews = 37,
                description = "Buku Makanya, Mikir! karya Abigail Limuria dan Cania Citta berisi kumpulan kerangka berpikir dan model mental yang membantu pembaca membuat keputusan yang lebih baik dalam hidup sehari-hari. Buku ini tidak memberikan jawaban langsung, tetapi mengajarkan cara berpikir efektif untuk menyelesaikan masalah sendiri dengan menggunakan berbagai model mental seperti penentuan cost dan benefit, pola pikir ilmiah, dan prinsip objective-oriented. Buku ini dilengkapi ilustrasi dan ruang interaktif agar mudah dipahami dan diterapkan dalam berbagai aspek kehidupan, termasuk karier, hubungan, dan tujuan hidup.",
                tags = arrayOf("Critical Thinking", "Logic", "Decision Making", "Self-Help"),
                backgroundColorResId = R.color.bg_book_makanya_mikir
            ),
            Book(
                title = "The Psychology of Money",
                author = "MORGAN HOUSEL",
                imageRes = R.drawable.book_psychology_of_money,
                rating = 5f,
                reviews = 37,
                description = "\"The Psychology of Money\" adalah: Sukses mengelola uang lebih banyak ditentukan oleh cara Anda berperilaku (psikologi dan emosi) daripada oleh seberapa pintar atau berpengetahuannya Anda.\n" +
                        "Morgan Housel berpendapat bahwa perilaku keuangan adalah soft skill, bukan hard science seperti fisika atau matematika. Di dunia nyata, orang tidak mengambil keputusan finansial hanya berdasarkan rumus atau spreadsheet. Mereka mengambil keputusan di meja makan, di tengah pertengkaran, atau saat terpengaruh oleh gengsi.Melalui 19 cerita pendek (bab), buku ini menjelaskan bagaimana emosi, bias psikologis, keserakahan, ketakutan, dan ego sangat memengaruhi cara kita menabung, membelanjakan uang, dan berinvestasi.",
                tags = arrayOf("Behavioral Finance", "Compounding", "Finance Freedom", "Personal Finance"),
                backgroundColorResId = R.color.bg_book_The_Psychology_of_Money
            ),
            Book(
                title = "Educated",
                author = "Tara Westover",
                imageRes = R.drawable.book_educated,
                rating = 5f,
                reviews = 37,
                description = "\"Educated\" adalah memoar inspiratif karya Tara Westover.\n" +
                        "Buku ini menceritakan kisah nyata perjalanannya yang luar biasa, dari tumbuh besar di keluarga survivalis yang terisolasi di Idaho―tanpa akta kelahiran dan tanpa pernah bersekolah hingga perjuangannya belajar sendiri yang membawanya meraih gelar PhD dari Universitas Cambridge.\n" +
                        "Ini adalah kisah tentang kekuatan transformatif pendidikan dalam menemukan jati diri dan membebaskan diri dari dogma.",
                tags = arrayOf("Education", "Self-Discovery", "Power of Education", "Liberation"),
                backgroundColorResId = R.color.bg_book_Educated
            ),
            Book(
                title = "Seporsi Mie Ayam Sebelum Mati",
                author = "Brian Khrisna",
                imageRes = R.drawable.book_mie_ayam,
                rating = 5f,
                reviews = 52,
                description = "\"Seporsi Mie Ayam Sebelum Mati\" adalah novel karya Brian Khrisna yang mengangkat tema kesehatan mental dengan penceritaan yang dekat.\n" +
                        "Buku ini mengisahkan Ale, seorang pria yang didiagnosis depresi akut dan memutuskan untuk mengakhiri hidupnya. Namun, sebelum itu, ia memiliki satu keinginan terakhir: makan seporsi mie ayam.\n" +
                        "Perjalanan sederhana mencari mie ayam ini ternyata membawanya pada pertemuan, perenungan, dan pertanyaan baru tentang makna hidup, kesepian, dan penerimaan diri.",
                tags = arrayOf("Mental Health", "Suicidal Ideation", "Bullying", "Loneliness"),
                backgroundColorResId = R.color.bg_book_mie_ayam
            ),

            Book(
                title = "Filosofi Teras",
                author = "Dr. A. Setyo Wibowo",
                imageRes = R.drawable.filosofi_teras,
                rating = 5f,
                reviews = 52,
                description = "Filosofi Teras adalah buku karya Henry Manampiring yang mengajak pembaca untuk mengenal dan menerapkan ajaran Stoikisme dalam kehidupan sehari-hari. Melalui buku ini, Manampiring mengajarkan bagaimana cara berpikir dan bertindak lebih bijaksana dengan menghadapi masalah hidup secara tenang dan rasional. Ia menggabungkan filosofi kuno yang berasal dari Yunani dengan budaya Indonesia, sehingga pembaca dapat dengan mudah memahami konsep-konsep dasar Stoikisme seperti kontrol diri, penerimaan terhadap takdir, dan pentingnya hidup sesuai dengan nilai-nilai yang baik.\n" +
                        "\n" +
                        "Buku ini memberikan panduan praktis untuk mencapai kebahagiaan sejati dengan cara mengendalikan pikiran dan perasaan, serta memilih respons yang lebih baik terhadap segala situasi. Filosofi Teras tidak hanya menjadi sebuah bacaan tentang teori-teori filsafat, tetapi juga sebuah pedoman hidup yang dapat membantu kita menjalani hidup dengan lebih damai dan penuh makna.",
                tags = arrayOf("Stoikisme", "Filosofi Hidup", "Kebahagiaan", "Pengembangan Diri"),
                backgroundColorResId = R.color.bg_book_default
            )
        )
        db.bookDao().insertAll(initialBooks)
    }

    companion object {
        const val EXTRA_USER_ROLE = "extra_user_role"
    }
}
