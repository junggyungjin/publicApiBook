package fastcampus.aop.part3.chapter04.publicApi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.GsonBuilder
import fastcampus.aop.part3.chapter04.publicApi.adapter.BookAdapter
import fastcampus.aop.part3.chapter04.publicApi.adapter.HistoryAdapter
import fastcampus.aop.part3.chapter04.publicApi.api.BookService
import fastcampus.aop.part3.chapter04.publicApi.databinding.ActivityMainBinding
import fastcampus.aop.part3.chapter04.publicApi.model.BestSellerDto
import fastcampus.aop.part3.chapter04.publicApi.model.Book
import fastcampus.aop.part3.chapter04.publicApi.model.History
import fastcampus.aop.part3.chapter04.publicApi.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclearView()
        initHistoryRecyclerView()
        initSearchEditText()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.interParkAPIKey))
            .enqueue(object: Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e("KKKK","NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d("KKKK",it.toString())

                        it.books.forEach { book ->
                            Log.d("AAAA",book.toString())
                        }

                        adapter.submitList(it.books) //submitList() 함수를 쓰게되면 리스트가 이걸로 체인지가 된다 TODO 공부 필요
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e("KKKK",t.toString())
                }

            })

    }

    private fun search(keyword: String) {
        bookService.getBookByName(getString(R.string.interParkAPIKey), keyword)
            .enqueue(object: Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {

                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        Log.d("KKKK","Not Success")
                        return
                    }

                    adapter.submitList(response.body()?.books.orEmpty()) //submitList() 함수를 쓰게되면 리스트가 이걸로 체인지가 된다 TODO 공부 필요


                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    hideHistoryView()
                }

            })
    }

    private fun initBookRecyclearView() {
        adapter = BookAdapter(itemClickedListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel",it)
            startActivity(intent)
        })
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter

//        initSearchEditText()
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { v, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true // return@setOnKeyListener true의 의미 : 내가 실제로 이 이벤트를 처리했음을 의미
            }
            return@setOnKeyListener false // 처리가 안된 if문에 걸리지 않은 나머지는 처리가 안되었다.
        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }

    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }

        }.start()
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}