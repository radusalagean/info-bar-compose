package com.radusalagean.infobarcompose

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

/**
 * Standard [InfoBar] message data structure.
 *
 * @param text Message as string
 * @param textStringResId Message as string resource id
 * @param textStringResArgs Arguments for [textStringResId]
 * @param textColor Color for the message text (overrides `textColor` set in the [InfoBar] composable)
 * @param action Action as string
 * @param actionStringResId Action as string resource id
 * @param actionStringResArgs Arguments for [actionStringResId]
 * @param actionColor Color for the action button text
 * (overrides `actionColor` set in the [InfoBar] composable)
 * @param backgroundColor see [BaseInfoBarMessage.backgroundColor]
 * @param displayTimeSeconds see [BaseInfoBarMessage.displayTimeSeconds]
 * @param onAction Function which is called when the user presses the action button
 */
class InfoBarMessage(
    val text: String? = null,
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

