package com.soulkey.craftsmanbartender.lib.view

import android.content.Context
import android.text.Layout
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.ceil

class TrimTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = ceil(getMaxLineWidth(layout)).toInt()
        val height = measuredHeight
        setMeasuredDimension(width, height)
    }

    private fun getMaxLineWidth(layout: Layout) : Float{
        var maximumWidth = 0f
        val lines = layout.lineCount
        for (i in 0..lines) {
            maximumWidth = Math.max(layout.getLineWidth(i), maximumWidth)
        }
        return maximumWidth
    }
}