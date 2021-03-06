package com.soulkey.craftsmanbartender.ui.adapter

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ItemRecipeListBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.lib.model.RecipeWithIngredient
import com.soulkey.craftsmanbartender.ui.recipe.RecipeDetailActivity
import android.util.Pair as UtilPair

class RecipeListAdapter : ListAdapter<RecipeWithIngredient, RecipeListAdapter.RecipeViewHolder>(object: DiffUtil.ItemCallback<RecipeWithIngredient>(){
    override fun areItemsTheSame(oldItem: RecipeWithIngredient, newItem: RecipeWithIngredient): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RecipeWithIngredient, newItem: RecipeWithIngredient): Boolean {
        return oldItem.basic.recipeId == newItem.basic.recipeId
    }
}) {
    class RecipeViewHolder(
            private val parent: ViewGroup,
            private val binding: ItemRecipeListBinding
    ) : ViewHolder(binding.root){
        fun bind(item: RecipeWithIngredient){
            binding.tvRecipeName.text = item.basic.name

            // Start RecipeDetailActivity by Selected Recipe
            binding.containerRecipeItem.setOnClickListener {
                Intent(parent.context, RecipeDetailActivity::class.java).also { intent->
                    intent.putExtra("recipeId", item.basic.recipeId)
                    val options = ActivityOptions.makeSceneTransitionAnimation(parent.context as BaseActivity,
//                            UtilPair.create(binding.tvRecipeName, "shared_recipe_name"),
                            UtilPair.create(binding.containerRecipeItem, "shared_recipe_container")
                    )
                    (parent.context as BaseActivity).startActivity(intent, options.toBundle())
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(parent, DataBindingUtil.inflate(
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