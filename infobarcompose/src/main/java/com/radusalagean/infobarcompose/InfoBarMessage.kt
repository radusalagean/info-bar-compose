package com.radusalagean.infobarcompose

import androidx.compose.ui.graphics.Color

class InfoBarMessage(
    val textString: String,
    val textColor: Color? = null,
    override val backgroundColor: Color? = null,
    override val displayTimeSeconds: Int? = 4
) : BaseInfoBarMessage()

