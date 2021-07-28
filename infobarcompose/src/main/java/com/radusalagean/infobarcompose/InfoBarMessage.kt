package com.radusalagean.infobarcompose

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

class InfoBarMessage(
    val text: String?,
    @StringRes val textStringResId: Int? = null,
    val textStringResArgs: Array<Any>? = null,
    val textColor: Color? = null,
    val action: String? = null,
    @StringRes val actionStringResId: Int? = null,
    val actionStringResArgs: Array<Any>? = null,
    val actionColor: Color? = null,
    override val backgroundColor: Color? = null,
    override val displayTimeSeconds: Int? = 4,
    val onAction: (() -> Unit)? = null
) : BaseInfoBarMessage() {

    override val containsControls: Boolean
        get() = action != null || actionStringResId != null

    @Composable
    fun getTextString(): String {
        return text ?: textStringResId?.let {
            textStringResArgs?.let {
                stringResource(textStringResId, *it)
            } ?: stringResource(textStringResId)
        } ?: ""
    }

    @Composable
    fun getActionString(): String? {
        return action ?: actionStringResId?.let {
            actionStringResArgs?.let {
                stringResource(actionStringResId, *it)
            } ?: stringResource(actionStringResId)
        }
    }
}

