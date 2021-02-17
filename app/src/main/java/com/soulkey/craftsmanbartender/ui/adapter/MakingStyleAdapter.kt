package com.soulkey.craftsmanbartender.ui.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class MakingStyleAdapter<T>(context: Context, textViewResourceId: Int, objects: List<T>) : ArrayAdapter<T>(context, textViewResourceId, objects) {
    private val filter: Filter = MakingStyleFilter()
    var items: List<T> = objects
    override fun getFilter(): Filter {
        return filter
    }

    private inner class MakingStyleFilter : Filter() {
        override fun performFiltering(char: CharSequence?): FilterResults {
            val result = FilterResults()
            result.values = items
            result.count = items.size
            return result
        }

        override fun publishResults(char: CharSequence?, result: FilterResults?) {
            notifyDataSetChanged()
        }
    }

}