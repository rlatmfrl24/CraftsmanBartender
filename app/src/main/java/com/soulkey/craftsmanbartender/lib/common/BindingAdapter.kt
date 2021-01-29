package com.soulkey.craftsmanbartender.lib.common

import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.soulkey.craftsmanbartender.lib.common.Constants.Companion.MakingStyle

class BindingAdapter {
    object MakingStyleConverter {
        @BindingAdapter("text")
        @JvmStatic
        fun setMakingStyle(view: AutoCompleteTextView, value: MakingStyle?){
            if (view.text.toString() != value?.name) {
                view.setText(value?.name)
            }
        }

        @InverseBindingAdapter(attribute = "text")
        @JvmStatic
        fun getMakingStyle(view: AutoCompleteTextView) : MakingStyle{
            return MakingStyle.valueOf(view.text.toString())
        }

        @BindingAdapter("textAttrChanged")
        @JvmStatic
        fun setMakingStyleListener(
            view: AutoCompleteTextView,
            attrChange: InverseBindingListener
        ) {
            view.setOnItemClickListener { _, _, _, _ ->
                attrChange.onChange()
            }
        }
    }
}