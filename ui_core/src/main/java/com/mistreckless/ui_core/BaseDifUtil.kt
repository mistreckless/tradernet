package com.mistreckless.ui_core

import androidx.recyclerview.widget.DiffUtil


class BaseDiffUtil<T : Any>(
    private val oldItems: List<T>,
    private val newItems: List<T>,
    private val areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    private val areContentsTheSame: (oldItem: T, newItem: T) -> Boolean,
    private val payloadChange: (oldItem: T, newItem: T) -> Any?
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areContentsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
        payloadChange(oldItems[oldItemPosition], newItems[newItemPosition])

}