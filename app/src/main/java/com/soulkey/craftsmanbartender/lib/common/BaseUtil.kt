package com.soulkey.craftsmanbartender.lib.common

import android.graphics.Color
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout

class BaseUtil {
    companion object {
        fun TextInputLayout.makeRequiredInRed(){
            hint = buildSpannedString {
                append(hint)
                color(Color.RED) { append(" *") }
            }
        }
        fun <T> MediatorLiveData<T>.addSourceList(vararg liveDataArgument: MutableLiveData<*>, onChanged: () -> T) {
            liveDataArgument.forEach {
                this.addSource(it) { value = onChanged() }
            }
        }

    }
}