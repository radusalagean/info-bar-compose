package com.radusalagean.infobarcompose

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class InfoBarMessage private constructor(
    val textString: String? = null,
    @StringRes val textStringRes: Int? = null,
    val textStringArgs: List<Any>? = null,
    val textColor: Color? = null,
    val backgroundColor: Color? = null,
    val displayTimeSeconds: Int?,
    val creationTime: Long = System.currentTimeMillis()
) {

    companion object {

        fun create(
            textString: String,
            textColor: Color? = null,
            backgroundColor: Color? = null,
            displayTimeSeconds: Int? = 4
        ) = InfoBarMessage(
            textString = textString,
            textColor = textColor,
            backgroundColor = backgroundColor,
            displayTimeSeconds = displayTimeSeconds
        )

        fun create(
            textStringRes: Int,
            textStringArgs: List<Any>? = null,
            textColor: Color? = null,
            backgroundColor: Color? = null,
            displayTimeSeconds: Int? = 4
        ) = InfoBarMessage(
            textStringRes = textStringRes,
            textStringArgs = textStringArgs,
            textColor = textColor,
            backgroundColor = backgroundColor,
            displayTimeSeconds = displayTimeSeconds
        )
    }
}

