package com.radusalagean.infobarcompose

import androidx.annotation.VisibleForTesting

object InfoBarDelay {

    private const val SHOW_DELAY_DEFAULT_VALUE = 200L

    internal var showDelay: Long = SHOW_DELAY_DEFAULT_VALUE
        private set

    @VisibleForTesting
    internal fun setNoDelay() {
        showDelay = 0
    }

    @VisibleForTesting
    internal fun resetDelayToDefault() {
        showDelay = SHOW_DELAY_DEFAULT_VALUE
    }
}