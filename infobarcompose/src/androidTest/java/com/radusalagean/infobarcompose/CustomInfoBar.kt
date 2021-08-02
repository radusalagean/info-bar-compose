package com.radusalagean.infobarcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal val customContent: @Composable (CustomInfoBarMessage) -> Unit = { message ->
    Box(
        modifier = Modifier.background(
            brush = Brush.linearGradient(
                0f to message.gradientStartColor,
                1f to message.gradientEndColor
            )
        )
    ) {
        Text(message.text)
    }
}

internal class CustomInfoBarMessage(
    val text: String,
    val gradientStartColor: Color,
    val gradientEndColor: Color
) : BaseInfoBarMessage() {

    override val containsControls: Boolean
        get() = false
}