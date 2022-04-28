package fastcampus.aop.part3.chapter04.publicApi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import fastcampus.aop.part3.chapter04.publicApi.adapter.BookAdapter
import fastcampus.aop.part3.chapter04.publicApi.api.BookService
import fastcampus.aop.part3.chapter04.publicApi.databinding.ActivityMainBinding
import fastcampus.aop.part3.chapter04.publicApi.model.BestSellerDto
import fastcampus.aop.part3.chapter04.publicApi.model.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclearView()

        var gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("BB916F3DC6A4EB788A5E7C5F0A111F8D14A587DB79B0292E1BA671991700EC97")
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

    fun initBookRecyclearView() {
        adapter = BookAdapter()
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}