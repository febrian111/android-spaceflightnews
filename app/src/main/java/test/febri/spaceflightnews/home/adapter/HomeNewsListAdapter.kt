package test.febri.spaceflightnews.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.BlogModel
import test.febri.domain.model.ReportModel
import test.febri.spaceflightnews.databinding.ItemNewsHomeBinding

class HomeNewsListAdapter() : ListAdapter<Any, HomeNewsListAdapter.HomeNewsViewHolder>(DiffCallback) {

    companion object {
        private const val TYPE_ARTICLE = 0
        private const val TYPE_BLOG = 1
        private const val TYPE_REPORT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNewsViewHolder {

        val binding = ItemNewsHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeNewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HomeNewsViewHolder(private val binding: ItemNewsHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Any) {
            when (item) {
                is ArticleModel -> {
                    Glide.with(binding.ivNewsImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(binding.ivNewsImage)
                    binding.tvNewsTitle.text = item.title
                }
                is BlogModel -> {
                    Glide.with(binding.ivNewsImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(binding.ivNewsImage)
                    binding.tvNewsTitle.text = item.title
                }
                is ReportModel -> {
                    Glide.with(binding.ivNewsImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(binding.ivNewsImage)
                    binding.tvNewsTitle.text = item.title
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is ArticleModel && newItem is ArticleModel -> oldItem.id == newItem.id
                oldItem is BlogModel && newItem is BlogModel -> oldItem.id == newItem.id
                oldItem is ReportModel && newItem is ReportModel -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is ArticleModel && newItem is ArticleModel -> (oldItem.id == newItem.id && oldItem.title == newItem.title)
                oldItem is BlogModel && newItem is BlogModel -> (oldItem.id == newItem.id && oldItem.title == newItem.title)
                oldItem is ReportModel && newItem is ReportModel -> (oldItem.id == newItem.id && oldItem.title == newItem.title)
                else -> false
            }
        }
    }
}
