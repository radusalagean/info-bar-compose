package com.radusalagean.infobarcompose.sample

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import com.radusalagean.infobarcompose.InfoBarMessage
import com.radusalagean.infobarcompose.InfoBarSlideEffect
import com.radusalagean.infobarcompose.sample.ui.components.GroupConfig
import com.radusalagean.infobarcompose.sample.ui.theme.CustomMessage1ActionColor
import com.radusalagean.infobarcompose.sample.ui.theme.CustomMessage1TextColor
import com.radusalagean.infobarcompose.sample.ui.theme.CustomMessage2ActionColor
import com.radusalagean.infobarcompose.sample.ui.theme.CustomMessage2TextColor

class MainViewModel : ViewModel() {

    var infoBarMessage: InfoBarMessage? by mutableStateOf(null)
        private set
    var customInfoBarMessage: CustomInfoBarMessage? by mutableStateOf(null)
        private set
    val infoBarAlignment: Alignment by derivedStateOf {
        MessagePositionRadioGroupOptions.values()[
                messagePositionRadioGroup.selectedIndex
        ].targetAlignment
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
            else -> MessagePositionRadioGroupOptions.values()[
                    messagePositionRadioGroup.selectedIndex
            ].slideEffect
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
    val messageTimeoutRadioGroup = GroupConfig.RadioGroupConfig(
        groupTitle = R.string.radio_group_message_timeout,
        options = MessageTimeoutRadioGroupOptions.values().map { it.stringResId },
        initialIndex = MessageTimeoutRadioGroupOptions.FOUR_SECONDS.ordinal
    )
    val messageActionRadioGroup = GroupConfig.RadioGroupConfig(
        groupTitle = R.string.radio_group_message_action,
        options = MessageActionRadioGroupOptions.values().map { it.stringResId },
        initialIndex = MessageActionRadioGroupOptions.DISMISS_ACTION.ordinal
    )

    var textField: String by mutableStateOf("Example message")
        private set

    private fun generateMessage() = InfoBarMessage(
        text = textField,
        actionStringResId = if (hasDismissAction()) R.string.dismiss_button else null,
        displayTimeSeconds = getTimeout()
    ) {
        infoBarMessage = null
    }

    private fun generateCustomMessage1() = CustomInfoBarMessage(
        bannerImage = R.drawable.banner_1,
        textString = textField,
        textColor = CustomMessage1TextColor,
        actionStringResId = if (hasDismissAction()) R.string.dismiss_button else null,
        actionColor = CustomMessage1ActionColor,
        displayTimeSeconds = getTimeout()
    ) {
        customInfoBarMessage = null
    }

    private fun generateCustomMessage2() = CustomInfoBarMessage(
        bannerImage = R.drawable.banner_2,
        textString = textField,
        textColor = CustomMessage2TextColor,
        actionStringResId = if (hasDismissAction()) R.string.dismiss_button else null,
        actionColor = CustomMessage2ActionColor,
        displayTimeSeconds = getTimeout()
    ) {
        customInfoBarMessage = null
    }

    private fun getTimeout(): Int {
        return MessageTimeoutRadioGroupOptions.values()[
                messageTimeoutRadioGroup.selectedIndex
        ].timeoutInSeconds
    }

    private fun hasDismissAction(): Boolean = MessageActionRadioGroupOptions.values()[
            messageActionRadioGroup.selectedIndex
    ] == MessageActionRadioGroupOptions.DISMISS_ACTION

    fun onTextValueChange(newText: String) {
        textField = newText
    }

    fun onShowMessageClick() {
        removeMessages()
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

    private fun removeMessages() {
        infoBarMessage = null
        customInfoBarMessage = null
    }

    fun onInfoBarMessageTimeout() {
        infoBarMessage = null
    }

    fun onCustomInfoBarMessageTimeout() {
        customInfoBarMessage = null
    }

    companion object RadioGroups {
        enum class MessagePositionRadioGroupOptions(
            @StringRes val stringResId: Int,
            val targetAlignment: Alignment,
            val slideEffect: InfoBarSlideEffect
        ) {
            TOP(R.string.radio_group_message_position_top, Alignment.TopCenter, InfoBarSlideEffect.FROM_TOP),
            BOTTOM(R.string.radio_group_message_position_bottom, Alignment.BottomCenter, InfoBarSlideEffect.FROM_BOTTOM)
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

        enum class MessageTimeoutRadioGroupOptions(
            @StringRes val stringResId: Int,
            val timeoutInSeconds: Int
        ) {
            FOUR_SECONDS(R.string.radio_group_message_timeout_4_seconds, 4),
            TEN_SECONDS(R.string.radio_group_message_timeout_10_seconds, 10),
            INDEFINITE(R.string.radio_group_message_timeout_indefinite, -1)
        }

        enum class MessageActionRadioGroupOptions(@StringRes val stringResId: Int) {
            NO_ACTION(R.string.radio_group_message_action_no_action),
            DISMISS_ACTION(R.string.radio_group_message_action_dismiss)
        }
    }
}