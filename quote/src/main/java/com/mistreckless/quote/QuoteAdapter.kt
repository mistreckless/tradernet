package com.mistreckless.quote

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mistreckless.ui_core.BaseDiffUtil
import com.mistreckless.ui_core.GlideRequests

class QuoteAdapter(private val glideRequests: GlideRequests) :
    RecyclerView.Adapter<QuoteViewHolder>() {

    private val items = mutableListOf<QuoteModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder =
        QuoteViewHolder(parent, glideRequests)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: QuoteViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(items[position], payloads)
    }

    override fun onViewRecycled(holder: QuoteViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
    }

    fun setItems(newItems: List<QuoteModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItems(newItems: List<QuoteModel>) {
        val diff = DiffUtil.calculateDiff(quoteDiff(items, newItems))
        items.clear()
        items.addAll(newItems)
        diff.dispatchUpdatesTo(this)
    }

    private fun quoteDiff(oldItems: List<QuoteModel>, newItems: List<QuoteModel>) =
        BaseDiffUtil<QuoteModel>(
            oldItems = oldItems,
            newItems = newItems,
            areItemsTheSame = { oldItem, newItem -> oldItem.ticker == newItem.ticker },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
            payloadChange = { oldItem, newItem -> Bundle() }
        )
}