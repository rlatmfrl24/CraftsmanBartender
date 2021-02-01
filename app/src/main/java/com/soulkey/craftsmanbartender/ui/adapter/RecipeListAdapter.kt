package com.soulkey.craftsmanbartender.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ItemRecipeListBinding
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient

class RecipeListAdapter : ListAdapter<RecipeWithIngredient, RecipeListAdapter.RecipeViewHolder>(object: DiffUtil.ItemCallback<RecipeWithIngredient>(){
    override fun areItemsTheSame(oldItem: RecipeWithIngredient, newItem: RecipeWithIngredient): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RecipeWithIngredient, newItem: RecipeWithIngredient): Boolean {
        return oldItem.basic.recipeBasicId == newItem.basic.recipeBasicId
    }
}) {
    class RecipeViewHolder(
            private val binding: ItemRecipeListBinding
    ) : ViewHolder(binding.root){
        fun bind(item: RecipeWithIngredient){
            binding.tvRecipeName.text = item.basic.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recipe_list,
                parent,
                false
        ))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}