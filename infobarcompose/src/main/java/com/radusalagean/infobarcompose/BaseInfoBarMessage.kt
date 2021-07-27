package com.radusalagean.infobarcompose

import androidx.compose.ui.graphics.Color

abstract class BaseInfoBarMessage {
    open val backgroundColor: Color? = null
    open val displayTimeSeconds: Int? = 4
}