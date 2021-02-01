package com.soulkey.craftsmanbartender.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ItemIngredientListBinding
import com.soulkey.craftsmanbartender.lib.model.Ingredient

class IngredientListAdapter : ListAdapter<Ingredient, IngredientListAdapter.IngredientListViewHolder>(object : DiffUtil.ItemCallback<Ingredient>(){
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.ingredientId == newItem.ingredientId
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}) {

    inner class IngredientListViewHolder(
        private val binding: ItemIngredientListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ingredient)  {
            binding.tvIngredientName.text = item.name
            binding.tvIngredientAmount.text = item.amount.toString()
            binding.tvIngredientUnit.text = item.unit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientListViewHolder {
        return IngredientListViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_ingredient_list,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: IngredientListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}