package com.radusalagean.infobarcompose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AccessibilityManager
import java.util.concurrent.TimeUnit

/**
 * Base class for any [InfoBar] message data structure.
 *
 * Extend this class to implement your own message data structure, when using the generic
 * [InfoBar] signature.
 *
 * @property containsControls Flag which signals whether the message contains on-screen controls
 * (e.g. a button) that the user can press. This is used for accessibility purposes, as some users
 * need more time to interact with interactive elements. You have to override this in your subclass.
 * @property backgroundColor Background color to be applied to the [InfoBar] surface
 * (overrides `backgroundColor` set in the [InfoBar] composable)
 * @property displayTimeSeconds The number of seconds to display the message
 * (excluding animation time). Pass `-1` if you don't want the message to time out.
 */
abstract class BaseInfoBarMessage {

    abstract val containsControls: Boolean
    open val backgroundColor: Color? = null
    open val displayTimeSeconds: Int? = 4

    internal fun getInfoBarTimeout(
        accessibilityManager: AccessibilityManager?
    ): Long {
        return displayTimeSeconds.takeUnless { it == null || it <= 0 }?.let {
            val originalMillis = TimeUnit.SECONDS.toMillis(it.toLong())
            accessibilityManager?.calculateRecommendedTimeoutMillis(
                originalTimeoutMillis = originalMillis,
                containsIcons = true,
                containsText = true,
                containsControls = containsControls
            ) ?: originalMillis
        } ?: Long.MAX_VALUE
    }
}