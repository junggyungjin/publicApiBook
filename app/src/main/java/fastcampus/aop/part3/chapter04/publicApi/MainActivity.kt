package fastcampus.aop.part3.chapter04.publicApi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import fastcampus.aop.part3.chapter04.publicApi.api.BookService
import fastcampus.aop.part3.chapter04.publicApi.model.BestSellerDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e("KKKK",t.toString())
                }

            })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}