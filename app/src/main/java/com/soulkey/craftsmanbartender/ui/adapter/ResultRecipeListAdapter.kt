package com.soulkey.craftsmanbartender.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ItemResultRecipeListBinding
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import kotlinx.android.synthetic.main.dialog_recipe_hint.*

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
                // TODO Show Recipe Data
                //show recipe hint
                MaterialDialog(context)
                        .title(text = item.basic.name)
                        .customView(R.layout.dialog_recipe_hint, scrollable = true, horizontalPadding = true)
                        .apply {
                            val ingredientAdapter = IngredientListAdapter()
                            tv_making_style.text = item.basic.combineMakingStylesToString()
                            tv_glass.text = item.basic.glass
                            tv_garnish.text = item.basic.garnish
                            recycler_recipe_ingredients.adapter = ingredientAdapter
                            ingredientAdapter.submitList(item.ingredients)
                        }
                        .positiveButton()
                        .show()
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