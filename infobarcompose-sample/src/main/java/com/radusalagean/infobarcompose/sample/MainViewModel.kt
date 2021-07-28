package com.radusalagean.infobarcompose.sample

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.radusalagean.infobarcompose.InfoBarMessage
import com.radusalagean.infobarcompose.InfoBarSlideEffect

class MainViewModel : ViewModel() {

    var infoBarMessage: InfoBarMessage? by mutableStateOf(null)
        private set
    var customInfoBarMessage: CustomInfoBarMessage? by mutableStateOf(null)
        private set
    var infoBarAlignment: Alignment by mutableStateOf(Alignment.BottomCenter)
    val infoBarSlideEffect: InfoBarSlideEffect by derivedStateOf {
        when(infoBarAlignment) {
            Alignment.BottomCenter -> InfoBarSlideEffect.FROM_BOTTOM
            else -> InfoBarSlideEffect.FROM_TOP
        }
    }

    var textField: String by mutableStateOf("Example message")
        private set

    private fun generateMessage() = InfoBarMessage(
        text = textField,
        actionStringResId = R.string.dismiss_button
    ) {
        infoBarMessage = null
    }

    private fun generateCustomMessage1() = CustomInfoBarMessage(
        bannerImage = R.drawable.banner_1,
        textString = textField,
        textColor = Color(0XFF084C61),
        actionStringResId = R.string.dismiss_button,
        actionColor = Color(0xFF0FA7D6),
        displayTimeSeconds = 4
    ) {
        customInfoBarMessage = null
    }

    private fun generateCustomMessage2() = CustomInfoBarMessage(
        bannerImage = R.drawable.banner_2,
        textString = textField,
        textColor = Color(0xFFA80D0D),
        actionStringResId = R.string.dismiss_button,
        actionColor = Color(0xFFF03535),
        displayTimeSeconds = 4
    ) {
        customInfoBarMessage = null
    }

    fun onTextValueChange(newText: String) {
        textField = newText
    }

    fun onShowMessageClick() {
        if (textField.isNotBlank()) {
            infoBarMessage = generateMessage()
//            customInfoBarMessage = generateCustomMessage2()
        }
    }

    fun onInfoBarMessageTimeout() {
        infoBarMessage = null
    }

    fun onCustomInfoBarMessageTimeout() {
        customInfoBarMessage = null
    }
}