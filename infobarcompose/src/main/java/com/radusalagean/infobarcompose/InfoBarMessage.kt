package com.radusalagean.infobarcompose

import androidx.compose.ui.graphics.Color

data class InfoBarMessage(
    val textString: String,
    val textColor: Color? = null,
    val backgroundColor: Color? = null,
    val displayTimeSeconds: Int? = 4,
    val creationTime: Long = System.currentTimeMillis()
)

