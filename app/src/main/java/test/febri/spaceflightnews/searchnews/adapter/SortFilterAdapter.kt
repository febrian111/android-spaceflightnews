package test.febri.spaceflightnews.searchnews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import test.febri.domain.model.SortFilterItemModel
import test.febri.spaceflightnews.databinding.ItemSortFilterBinding

class SortFilterAdapter(private val onOptionClick: (SortFilterItemModel) -> Unit) : RecyclerView.Adapter<SortFilterAdapter.SortFilterItemViewHolder>() {

    private var listData = listOf< SortFilterItemModel>()

    fun submitList(list: List<SortFilterItemModel>) {
        listData = list
    }

    inner class SortFilterItemViewHolder(private val binding: ItemSortFilterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SortFilterItemModel) {
            binding.root.setOnClickListener {
                onOptionClick.invoke(data)
            }
            binding.tvLabel.text = data.label
            binding.ivSelected.visibility = if (data.isSelected) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortFilterItemViewHolder {
        val binding = ItemSortFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SortFilterItemViewHolder(binding)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: SortFilterItemViewHolder, position: Int) {
        holder.bind(listData[position])
    }
}