package com.radusalagean.infobarcompose.sample.ui.components

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class GroupConfig(
    @StringRes val groupTitle: Int,
    @StringRes val options: List<Int>
) {
    abstract fun onIndexSelect(newIndex: Int)

    class RadioGroupConfig(
        @StringRes groupTitle: Int,
        @StringRes options: List<Int>,
        initialIndex: Int = -1
    ) : GroupConfig(
        groupTitle = groupTitle,
        options = options
    ) {
        var selectedIndex: Int by mutableStateOf(initialIndex)
            private set

        override fun onIndexSelect(newIndex: Int) {
            selectedIndex = newIndex
        }
    }

    class CheckGroupConfig(
        @StringRes groupTitle: Int,
        @StringRes options: List<Int>,
        initialIndices: List<Boolean>
    ) : GroupConfig(
        groupTitle = groupTitle,
        options = options
    ) {
        var selectedIndices: List<Boolean> by mutableStateOf(initialIndices)

        override fun onIndexSelect(newIndex: Int) {
            val selInd = selectedIndices.toMutableList()
            selInd[newIndex] = !selInd[newIndex]
            selectedIndices = selInd
        }
    }
}