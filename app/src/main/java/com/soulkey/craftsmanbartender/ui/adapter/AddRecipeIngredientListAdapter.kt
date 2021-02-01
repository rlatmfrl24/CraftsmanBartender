package com.soulkey.craftsmanbartender.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ItemAddRecipeIngredientListBinding
import com.soulkey.craftsmanbartender.lib.common.Constants
import com.soulkey.craftsmanbartender.lib.model.Ingredient
import com.soulkey.craftsmanbartender.ui.recipe.RecipeViewModel

class AddRecipeIngredientListAdapter(private val viewModel: RecipeViewModel) : ListAdapter<Ingredient, AddRecipeIngredientListAdapter.AddRecipeIngredientListViewHolder>(object: DiffUtil.ItemCallback<Ingredient>(){
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.ingredientId == newItem.ingredientId
    }
}) {

    inner class AddRecipeIngredientListViewHolder(
        private val binding: ItemAddRecipeIngredientListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ingredient) {
            binding.tvIngredientName.text = item.name

            when(Constants.Companion.IngredientUnit.valueOf(item.unit)) {
                // Data View Type Setting by Ingredient Unit Type
                Constants.Companion.IngredientUnit.fill -> {
                    binding.tvIngredientAmount.visibility = View.GONE
                    binding.tvIngredientUnit.text = "Fill-Up"
                }
                else -> {
                    // Check Decimal Data (Non-Decimal to Int)
                    val amount = item.amount ?: return
                    if (amount % 1 != 0f) {
                        binding.tvIngredientAmount.text = item.amount.toString()
                    } else {
                        binding.tvIngredientAmount.text = amount.toInt().toString()
                    }
                    binding.tvIngredientUnit.text = item.unit
                }
            }

            // Remove Ingredient Button Action
            binding.ivRemoveIngredient.setOnClickListener {
                viewModel.removeIngredient(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRecipeIngredientListViewHolder {
        return AddRecipeIngredientListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_add_recipe_ingredient_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddRecipeIngredientListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}