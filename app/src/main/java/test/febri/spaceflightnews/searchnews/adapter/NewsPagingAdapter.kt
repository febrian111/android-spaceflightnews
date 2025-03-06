package test.febri.spaceflightnews.searchnews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import test.febri.domain.model.ArticleModel
import test.febri.spaceflightnews.databinding.ItemNewsSearchBinding


class NewsPagingAdapter : PagingDataAdapter<ArticleModel, NewsPagingAdapter.ArticleViewHolder>(ARTICLE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemNewsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        article?.let { holder.bind(it) }
    }

    inner class ArticleViewHolder(private val binding: ItemNewsSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleModel) {
            binding.tvNewsTitle.text = article.title
            binding.tvNewsSummary.text = article.summary.split('.')[0]
            binding.tvPublishDate.text = article.publishedAt

            // Load image using Glide
            Glide.with(binding.ivNewsImage)
                .load(article.imageUrl)
                .centerCrop()
                .into(binding.ivNewsImage)

            // Add launches and events to Chips
            binding.chipGroup.addView(Chip(binding.root.context).apply {
                text = article.launches.size.toString() + " launches"
            })

            binding.chipGroup.addView(Chip(binding.root.context).apply {
                text = article.events.size.toString() + " events"
            })
        }
    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
