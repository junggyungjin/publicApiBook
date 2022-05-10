package fastcampus.aop.part3.chapter04.publicApi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fastcampus.aop.part3.chapter04.publicApi.databinding.ItemBookBinding
import fastcampus.aop.part3.chapter04.publicApi.databinding.ItemHistoryBinding
import fastcampus.aop.part3.chapter04.publicApi.model.Book
import fastcampus.aop.part3.chapter04.publicApi.model.History

class HistoryAdapter(val historyDeleteClickedListener: (String) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(
        HistoryAdapter.diffUtil
    ) {
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel: History) {
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickedListener(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        /**
         * Recyclerview의 데이터가 변하면 Recyclerview Adapter가 제공하는 notifyItem 메소드를 사용해서 ViewHolder 내용을 갱신할 수 있습니다.
         * 그런데 데이터가 변경되는 방식을 확인하고 그때마다 이렇게 notify를 일일이 해 주는것은 번거롭기도 하고, 또 사용하기에 따라서는 갱신이 필요없는 ViewHolder를 같이 갱신하는 불필요한 작업이 생길수도 있습니다.
         * DiffUtil은 두 데이터셋을 받아서 그 차이를 계산해주는 클래스입니다. DiffUtil을 사용하면 두 데이터 셋을 비교한 뒤 그중 변한부분만을 파악하여 Recyclerview에 반영할 수 있습니다.
         */

        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}