package com.soulkey.craftsmanbartender.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.DialogRecipeHintBinding
import com.soulkey.craftsmanbartender.databinding.ItemResultRecipeListBinding
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.lib.view.ViewUtil

class ResultRecipeListAdapter(private val context: Context) : ListAdapter<RecipeWithIngredient, ResultRecipeListAdapter.ResultRecipeListViewHolder>(object : DiffUtil.ItemCallback<RecipeWithIngredient>(){
    override fun areItemsTheSame(
        oldItem: RecipeWithIngredient,
        newItem: RecipeWithIngredient
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: RecipeWithIngredient,
        newItem: RecipeWithIngredient
    ): Boolean {
        return oldItem.basic.recipeId == newItem.basic.recipeId
    }

}) {
    inner class ResultRecipeListViewHolder(
        private val binding: ItemResultRecipeListBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: RecipeWithIngredient) {
            binding.tvRecipeName.text = item.basic.name
            binding.root.setOnClickListener {
                ViewUtil.makeRecipeDialog(context, item).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultRecipeListViewHolder {
        return ResultRecipeListViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_result_recipe_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResultRecipeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}