package com.example.myapplication.presentation.paperadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.PaperListItemBinding
import com.example.myapplication.presentation.paperadapter.PaperScanItem.Companion.getNumberOfChildren

class PaperScanAdapter(
    private val onSelectedItemCountChanged: (Int) -> (Unit)
) : RecyclerView.Adapter<PaperScanAdapter.PaperScanViewHolder>() {

    private val items: MutableList<PaperScanItemWrapper> = mutableListOf()
    private lateinit var binding: PaperListItemBinding
    private var lastItemName = 0

    fun setAdapterItems(paperScanItems: List<PaperScanItem>) {
        items.addAll(paperScanItems.map { PaperScanItemWrapper(it, false, -1) })
        lastItemName = items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaperScanViewHolder {
        binding = PaperListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaperScanViewHolder(binding) { item ->
            onSelectedItemCountChanged(items.count { it.isSelected })
            item.selectedPosition = items.count { it.isSelected }
            notifyItemChanged(items.indexOf(item))
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PaperScanViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun organizeSelectedItem() {
        val selectedItems = items.filter { it.isSelected }
        val firstSelectedItem = selectedItems.first()
        val restOfSelectedItems = selectedItems.filter { it != firstSelectedItem }

        firstSelectedItem.paperScanItem.children = restOfSelectedItems.map { it.paperScanItem }
        removeSelectedItems(restOfSelectedItems)
        firstSelectedItem.isSelected = false
        notifyItemChanged(items.indexOf(firstSelectedItem))

        onSelectedItemCountChanged(selectedItems.size)
    }

    private fun removeSelectedItems(itemsToRemove: List<PaperScanItemWrapper>) {
        itemsToRemove.forEach {
            val removedItemIndex = items.indexOf(it)
            items.remove(it)
            notifyItemRemoved(removedItemIndex)
        }
    }

    fun fillWithItems(paperScanItemsCount: Int) {
        val currentItemSize = items.size
        val numberOfItemsToBeAdded = paperScanItemsCount - currentItemSize
        (1..numberOfItemsToBeAdded).forEach {
            items.add(
                PaperScanItemWrapper(
                    PaperScanItem((lastItemName + it).toString()),
                    false,
                    -1
                )
            )
        }
        lastItemName += numberOfItemsToBeAdded
    }

    class PaperScanViewHolder(
        private val itemBinding: PaperListItemBinding,
        private val onItemSelected: (PaperScanItemWrapper) -> (Unit)
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: PaperScanItemWrapper) {
            itemBinding.cardItem.setCardBackgroundColor(getCardBackgroundColor(item.isSelected))
            itemBinding.tvCardName.text = item.paperScanItem.displayText

            if (item.paperScanItem.children.isNotEmpty()) {
                itemBinding.tvChildCount.visibility = View.VISIBLE
                itemBinding.tvChildCount.text = getNumberOfChildren(item.paperScanItem).toString()
            } else {
                itemBinding.tvChildCount.visibility = View.GONE
            }
            if (item.isSelected) {
                itemBinding.tvCardSelectedIndex.visibility = View.VISIBLE
                itemBinding.tvCardSelectedIndex.text = (item.selectedPosition).toString()
            } else {
                itemBinding.tvCardSelectedIndex.visibility = View.GONE
            }

            itemBinding.root.setOnClickListener {
                item.isSelected = !item.isSelected
                itemBinding.cardItem.setCardBackgroundColor(
                    getCardBackgroundColor(isSelected = item.isSelected)
                )
                onItemSelected(item)
            }
        }

        private fun getCardBackgroundColor(isSelected: Boolean): Int {
            return if (isSelected) {
                ContextCompat.getColor(itemBinding.root.context, R.color.orange_ff)
            } else {
                ContextCompat.getColor(itemBinding.root.context, R.color.yellow_ff)
            }
        }
    }

    data class PaperScanItemWrapper(
        val paperScanItem: PaperScanItem, var isSelected: Boolean, var selectedPosition: Int
    )

}