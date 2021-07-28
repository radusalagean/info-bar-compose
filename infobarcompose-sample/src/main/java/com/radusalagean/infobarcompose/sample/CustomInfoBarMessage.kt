package com.radusalagean.infobarcompose.sample

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.radusalagean.infobarcompose.BaseInfoBarMessage

class CustomInfoBarMessage(
    @DrawableRes val bannerImage: Int,
    val textString: String,
    val textColor: Color? = null,
    @StringRes val actionStringResId: Int,
    val actionColor: Color? = null,
    val actionBackgroundColor: Color? = null,
    override val containsControls: Boolean = true,
    override val displayTimeSeconds: Int? = 4,
    val onAction: () -> Unit
) : BaseInfoBarMessage()