package com.radusalagean.infobarcompose

import androidx.compose.ui.graphics.Color

class InfoBarMessage(
    val text: String,
    val textColor: Color? = null,
    val action: String? = null,
    val actionColor: Color? = null,
    override val containsControls: Boolean = action != null,
    override val backgroundColor: Color? = null,
    override val displayTimeSeconds: Int? = 4,
    val onAction: (() -> Unit)? = null
) : BaseInfoBarMessage()

