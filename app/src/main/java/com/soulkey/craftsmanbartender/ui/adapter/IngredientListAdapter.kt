package com.soulkey.craftsmanbartender.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ItemAddRecipeIngredientBinding
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
        private val parent: ViewGroup,
        private val binding: ItemAddRecipeIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ingredient)  {
            binding.tvIngredientName.text = item.name
            binding.tvIngredientAmount.text = item.amount.toString()
            binding.tvIngredientUnit.text = item.unit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientListViewHolder {
        return IngredientListViewHolder(parent, DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_add_recipe_ingredient,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: IngredientListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}