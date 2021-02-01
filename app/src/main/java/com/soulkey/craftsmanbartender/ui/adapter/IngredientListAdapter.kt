package com.soulkey.craftsmanbartender.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ItemIngredientListBinding
import com.soulkey.craftsmanbartender.lib.common.Constants
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.IngredientUnit
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
        @SuppressLint("SetTextI18n")
        fun bind(item: Ingredient)  {
            binding.tvIngredientName.text = item.name

            when(IngredientUnit.valueOf(item.unit)) {
                IngredientUnit.fill -> {
                    binding.tvIngredientAmount.visibility = View.GONE
                    binding.tvIngredientUnit.text = "Fill-Up"
                }
                else -> {
                    val amount = item.amount ?: return
                    if (amount % 1 != 0f) {
                        binding.tvIngredientAmount.text = item.amount.toString()
                    } else {
                        binding.tvIngredientAmount.text = amount.toInt().toString()
                    }
                    binding.tvIngredientUnit.text = item.unit
                }
            }
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