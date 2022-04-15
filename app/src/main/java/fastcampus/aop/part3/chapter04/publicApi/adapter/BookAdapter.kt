package fastcampus.aop.part3.chapter04.publicApi.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.part3.chapter04.publicApi.databinding.ItemBookBinding
import fastcampus.aop.part3.chapter04.publicApi.model.Book

class BookAdapter: androidx.recyclerview.widget.ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    inner class BookItemViewHolder(private val binding: ItemBookBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}