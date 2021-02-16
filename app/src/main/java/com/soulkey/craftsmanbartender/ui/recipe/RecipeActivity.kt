package com.soulkey.craftsmanbartender.ui.recipe

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.databinding.ActivityRecipeBinding
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import com.soulkey.craftsmanbartender.ui.adapter.RecipeListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeActivity : BaseActivity() {

    private val recipeAdapter = RecipeListAdapter()
    private val recipeViewModel: RecipeViewModel by viewModel()
    private val binding : ActivityRecipeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_recipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        binding.adapter = recipeAdapter

        // Toolbar Menu Action
        binding.toolbarRecipe.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_recipe_reset -> {
                    // Load Base Recipes from Firestore
                    MaterialDialog(binding.root.context)
                            .title(text = "Recipe Reset")
                            .message(text = getString(R.string.string_warning_msg_recipe_reset))
                            .positiveButton {
                                lifecycleScope.launch {
                                    binding.progressbarRecipeLoading.visibility = View.VISIBLE
                                    recipeViewModel.resetRecipeList()
                                    binding.progressbarRecipeLoading.visibility = View.GONE
                                }
                            }.negativeButton()
                            .show()
                    true
                }
                else -> false
            }
        }

        // Apply Recipes to Recyclerview
        recipeViewModel.recipes.observe(this, { list->
            recipeAdapter.submitList(list)
        })

        // Add Recipe Fab Button action
        binding.fabAddRecipe.setOnClickListener {
            Intent(this, AddRecipeActivity::class.java).also { intent ->
                val options = ActivityOptions.makeSceneTransitionAnimation(this,
                        Pair.create(it, "shared_recipe_add")
                )
                startActivity(intent, options.toBundle())
            }
        }
    }
}