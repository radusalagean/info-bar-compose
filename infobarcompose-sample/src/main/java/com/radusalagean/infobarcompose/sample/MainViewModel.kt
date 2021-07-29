package com.radusalagean.infobarcompose.sample

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.radusalagean.infobarcompose.InfoBarMessage
import com.radusalagean.infobarcompose.InfoBarSlideEffect
import com.radusalagean.infobarcompose.sample.ui.components.GroupConfig

class MainViewModel : ViewModel() {

    var infoBarMessage: InfoBarMessage? by mutableStateOf(null)
        private set
    var customInfoBarMessage: CustomInfoBarMessage? by mutableStateOf(null)
        private set
    val infoBarAlignment: Alignment by derivedStateOf {
        when(messagePositionRadioGroup.selectedIndex) {
            MessagePositionRadioGroupOptions.TOP.ordinal -> Alignment.TopCenter
            else -> Alignment.BottomCenter
        }
    }
    val infoBarFadeEffect: Boolean by derivedStateOf {
        messageAnimationCheckGroup.selectedIndices[
                MessageAnimationCheckGroupOptions.FADE.ordinal
        ]
    }
    val infoBarSlideEffect: InfoBarSlideEffect by derivedStateOf {
        when {
            !messageAnimationCheckGroup.selectedIndices[
                    MessageAnimationCheckGroupOptions.SLIDE.ordinal
            ] -> InfoBarSlideEffect.NONE
            infoBarAlignment == Alignment.TopCenter -> InfoBarSlideEffect.FROM_TOP
            infoBarAlignment == Alignment.BottomCenter -> InfoBarSlideEffect.FROM_BOTTOM
            else -> InfoBarSlideEffect.NONE
        }
    }

    // Groups
    val messagePositionRadioGroup = GroupConfig.RadioGroupConfig(
        groupTitle = R.string.radio_group_message_position,
        options = MessagePositionRadioGroupOptions.values().map { it.stringResId },
        initialIndex = MessagePositionRadioGroupOptions.BOTTOM.ordinal
    )
    val messageTypeRadioGroup = GroupConfig.RadioGroupConfig(
        groupTitle = R.string.radio_group_message_type,
        options = MessageTypeRadioGroupOptions.values().map { it.stringResId },
        initialIndex = MessageTypeRadioGroupOptions.GENERIC.ordinal
    )
    val messageAnimationCheckGroup = GroupConfig.CheckGroupConfig(
        groupTitle = R.string.check_group_message_animation,
        options = MessageAnimationCheckGroupOptions.values().map { it.stringResId },
        initialIndices = MessageAnimationCheckGroupOptions.values().map { it.initialValue }
    )

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
            when(messageTypeRadioGroup.selectedIndex) {
                MessageTypeRadioGroupOptions.CUSTOM_1.ordinal ->
                    customInfoBarMessage = generateCustomMessage1()
                MessageTypeRadioGroupOptions.CUSTOM_2.ordinal ->
                    customInfoBarMessage = generateCustomMessage2()
                MessageTypeRadioGroupOptions.GENERIC.ordinal ->
                    infoBarMessage = generateMessage()
            }
        }
    }

    fun onInfoBarMessageTimeout() {
        infoBarMessage = null
    }

    fun onCustomInfoBarMessageTimeout() {
        customInfoBarMessage = null
    }

    companion object RadioGroups {
        enum class MessagePositionRadioGroupOptions(@StringRes val stringResId: Int) {
            TOP(R.string.radio_group_message_position_top),
            BOTTOM(R.string.radio_group_message_position_bottom)
        }

        enum class MessageTypeRadioGroupOptions(@StringRes val stringResId: Int) {
            GENERIC(R.string.radio_group_message_type_generic),
            CUSTOM_1(R.string.radio_group_message_type_custom_1),
            CUSTOM_2(R.string.radio_group_message_type_custom_2)
        }

        enum class MessageAnimationCheckGroupOptions(
            @StringRes val stringResId: Int,
            val initialValue: Boolean
        ) {
            FADE(R.string.check_group_message_animation_fade, true),
            SLIDE(R.string.check_group_message_animation_slide, true)
        }
    }
}