package com.radusalagean.infobarcompose

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class InfoBarPadding private constructor(
    val start: Dp = 0.dp,
    val top: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp
) {

    companion object {

        val default = invoke(
            horizontal = 8.dp,
            vertical = 4.dp
        )

        operator fun invoke(
            start: Dp = 0.dp,
            top: Dp = 0.dp,
            end: Dp = 0.dp,
            bottom: Dp = 0.dp
        ) = InfoBarPadding(
            start = start,
            top = top,
            end = end,
            bottom = bottom
        )

        operator fun invoke(
            horizontal: Dp = 0.dp,
            vertical: Dp = 0.dp
        ) = InfoBarPadding(
            start = horizontal,
            top = vertical,
            end = horizontal,
            bottom = vertical
        )

        operator fun invoke(
            all: Dp = 0.dp
        ) = InfoBarPadding(
            start = all,
            top = all,
            end = all,
            bottom = all
        )
    }
}
