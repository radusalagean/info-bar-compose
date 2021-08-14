package com.radusalagean.infobarcompose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import com.radusalagean.infobarcompose.test.R
import io.mockk.*

/**
 * Gold screenshots are generated on a Pixel 5 Emulator - API 31
 *  (Run tests on that device)
 */
class InfoBarKtTest {

    // Test name format: GIVEN_WHEN_THEN

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun genericInfoBar_called_isDisplayed() {
        setContent { GenericInfoBarWithStringTitle() }
        waitForInfoBar()
    }

    @Test
    fun genericInfoBar_calledWithActionAndTappedAction_onActionCalled() {
        val onAction: () -> Unit = mockk(relaxed = true)
        setContent { GenericInfoBarWithStringTitleAndAction(onAction) }
        verify(exactly = 0) { onAction() }
        rule.onNodeWithText(EXAMPLE_ACTION).performClick()
        verify(exactly = 1) { onAction() }
    }

    @Test
    fun genericInfoBar_called_onMessageTimeoutCalledWhenTimedOut() {
        val onMessageTimeout: () -> Unit = mockk(relaxed = true)
        setContent { GenericInfoBarWithMockedOnMessageTimeout(onMessageTimeout = onMessageTimeout) }
        rule.onNodeWithContentDescription(INFO_BAR_CONTENT_DESCRIPTION).assertExists()
        verify(exactly = 0) { onMessageTimeout() }
        rule.waitUntil(2000) {
            rule.onAllNodesWithContentDescription(INFO_BAR_CONTENT_DESCRIPTION)
                .fetchSemanticsNodes().isEmpty()
        }
        verify(exactly = 1) { onMessageTimeout() }
    }

    // Screenshot-based tests

    @Test
    fun genericInfoBar_calledWithStringTitle_matchesScreenshot() {
        setContent { GenericInfoBarWithStringTitle() }
        checkAgainstScreenshot("generic_info_bar_with_string_title")
    }

    @Test
    fun genericInfoBar_calledWithStringTitleNoWrap_matchesScreenshot() {
        setContent { GenericInfoBarWithStringTitleNoWrap() }
        checkAgainstScreenshot("generic_info_bar_with_string_title_no_wrap")
    }

    @Test
    fun genericInfoBar_calledWithStringTitleAndAction_matchesScreenshot() {
        setContent { GenericInfoBarWithStringTitleAndAction() }
        checkAgainstScreenshot("generic_info_bar_with_string_title_and_action")
    }

    @Test
    fun genericInfoBar_calledWithOptionalStylingParams_matchesScreenshot() {
        setContent { GenericInfoBarWithOptionalStylingParams() }
        checkAgainstScreenshot("generic_info_bar_with_optional_styling_params")
    }

    @Test // Message styling should override composable styling
    fun genericInfoBar_calledWithOptionalStylingParamsInMessageAndComposable_matchesScreenshot() {
        setContent { GenericInfoBarWithOptionalStylingParamsInMessageAndComposable() }
        checkAgainstScreenshot("generic_info_bar_with_optional_styling_params_in_message_and_composable")
    }

    @Test
    fun genericInfoBar_calledWithStringResourceForTextAndAction_matchesScreenshot() {
        setContent { GenericInfoBarWithStringResourceForTextAndAction() }
        checkAgainstScreenshot("generic_info_bar_with_string_resource_for_text_and_action")
    }

    @Test
    fun genericInfoBar_calledWithStringResourceAndArgsForTextAndAction_matchesScreenshot() {
        setContent { GenericInfoBarWithStringResourceAndArgsForTextAndAction() }
        checkAgainstScreenshot("generic_info_bar_with_string_resource_and_args_for_text_and_action")
    }

    @Test
    fun genericInfoBar_calledWithNoTitleOrAction_matchesScreenshot() {
        setContent { GenericInfoBarWithNoTitleOrAction() }
        checkAgainstScreenshot("generic_info_bar_with_no_title_or_action")
    }

    @Test
    fun genericInfoBar_calledWithFadeEffectOnly_matchesScreenshot() {
        rule.mainClock.autoAdvance = false
        setContent { GenericInfoBarWithFadeEffectOnly() }
        advanceTimeBy(128)
        checkAgainstScreenshot("generic_info_bar_with_fade_effect_only_128ms_in")
    }

    @Test
    fun genericInfoBar_calledWithScaleEffectOnly_matchesScreenshot() {
        rule.mainClock.autoAdvance = false
        setContent { GenericInfoBarWithScaleEffectOnly() }
        advanceTimeBy(128)
        checkAgainstScreenshot("generic_info_bar_with_scale_effect_only_128ms_in")
    }

    @Test
    fun genericInfoBar_calledWithSlideFromTopEffectOnly_matchesScreenshot() {
        rule.mainClock.autoAdvance = false
        setContent { GenericInfoBarWithSlideFromTopEffectOnly() }
        advanceTimeBy(128)
        checkAgainstScreenshot("generic_info_bar_with_slide_from_top_effect_only_128ms_in")
    }

    @Test
    fun genericInfoBar_calledWithSlideFromBottomEffectOnly_matchesScreenshot() {
        rule.mainClock.autoAdvance = false
        setContent { GenericInfoBarWithSlideFromBottomEffectOnly() }
        advanceTimeBy(128)
        checkAgainstScreenshot("generic_info_bar_with_slide_from_bottom_effect_only_128ms_in")
    }

    @Test
    fun genericInfoBar_calledWithFadeAndScaleEffects_matchesScreenshot() {
        rule.mainClock.autoAdvance = false
        setContent { GenericInfoBarWithFadeAndScaleEffects() }
        advanceTimeBy(128)
        checkAgainstScreenshot("generic_info_bar_with_fade_and_scale_effects_128ms_in")
    }

    @Test
    fun genericInfoBar_calledWithAllEffects_matchesScreenshot() {
        rule.mainClock.autoAdvance = false
        setContent { GenericInfoBarWithAllEffects() }
        advanceTimeBy(128)
        checkAgainstScreenshot("generic_info_bar_with_all_effects_128ms_in")
    }

    @Test
    fun genericInfoBar_calledWithAllEffectsAndDelayedUntilTransitionsOut_matchesScreenshot() {
        rule.mainClock.autoAdvance = false
        setContent { GenericInfoBarWithAllEffects() }
        advanceTimeBy(4209)
        checkAgainstScreenshot("generic_info_bar_with_all_effects_4209ms_in")
    }

    @Test
    fun genericInfoBar_calledWhenAnotherMessageIsAlreadyDisplayed_matchesScreenshots() {
        rule.mainClock.autoAdvance = false
        var message: InfoBarMessage by mutableStateOf(InfoBarMessage(EXAMPLE_SHORT_STRING))
        setContent { GenericInfoBarWithFadeAndScaleEffects(message) }
        advanceTimeBy(2000)
        checkAgainstScreenshot("stacked_generic_info_bar_snap_1_2000ms_in")
        message = InfoBarMessage(EXAMPLE_LONG_STRING)
        advanceTimeBy(64)
        checkAgainstScreenshot("stacked_generic_info_bar_snap_2_another_64ms_in")
        advanceTimeBy(32)
        checkAgainstScreenshot("stacked_generic_info_bar_snap_3_another_32ms_in")
        advanceTimeBy(150)
        checkAgainstScreenshot("stacked_generic_info_bar_snap_4_another_150ms_in")
    }

    @Test
    fun customInfoBar_called_matchesScreenshot() {
        setContent { CustomInfoBarComposable() }
        checkAgainstScreenshot("custom_info_bar")
    }

    // Composables

    @Composable
    private fun GenericInfoBarWithStringTitle() {
        InfoBar(offeredMessage = InfoBarMessage(text = EXAMPLE_SHORT_STRING)) {}
    }

    @Composable
    private fun GenericInfoBarWithStringTitleNoWrap() {
        InfoBar(
            offeredMessage = InfoBarMessage(text = EXAMPLE_SHORT_STRING),
            wrapInsideExpandedBox = false
        ) {}
    }

    @Composable
    private fun GenericInfoBarWithStringTitleAndAction(onAction: () -> Unit = {}) {
        InfoBar(
            offeredMessage = InfoBarMessage(
                text = EXAMPLE_SHORT_STRING,
                action = EXAMPLE_ACTION,
                onAction = onAction
            )
        ) {}
    }

    @Composable
    private fun GenericInfoBarWithMockedOnMessageTimeout(onMessageTimeout: () -> Unit) {
        InfoBar(
            offeredMessage = InfoBarMessage(text = EXAMPLE_SHORT_STRING, displayTimeSeconds = 1),
            onDismiss = onMessageTimeout
        )
    }

    @Composable
    private fun GenericInfoBarWithOptionalStylingParams() {
        InfoBar(
            modifier = Modifier.padding(16.dp),
            offeredMessage = InfoBarMessage(text = EXAMPLE_LONG_STRING, action = EXAMPLE_ACTION),
            elevation = 24.dp,
            shape = CutCornerShape(bottomEnd = 12.dp),
            backgroundColor = Color(0xFFFFBC01),
            textColor = Color(0xFF241A00),
            textFontSize = 8.sp,
            textFontStyle = FontStyle.Italic,
            textFontWeight = FontWeight.Bold,
            textFontFamily = FontFamily.Serif,
            textLetterSpacing = 2.sp,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.End,
            textLineHeight = 36.sp,
            textMaxLines = 4,
            textStyle = MaterialTheme.typography.h4,
            actionColor = Color(0xFFFAF7E2)
        ) {}
    }

    @Composable
    private fun GenericInfoBarWithOptionalStylingParamsInMessageAndComposable() {
        InfoBar(
            offeredMessage = InfoBarMessage(
                text = EXAMPLE_SHORT_STRING,
                action = EXAMPLE_ACTION,
                textColor = Color.Red,
                actionColor = Color.Green,
                backgroundColor = Color.LightGray
            ),
            backgroundColor = Color(0xFFFFBC01),
            textColor = Color(0xFF241A00),
            actionColor = Color(0xFFFAF7E2)
        ) {}
    }

    @Composable
    private fun GenericInfoBarWithStringResourceForTextAndAction() {
        InfoBar(
            offeredMessage = InfoBarMessage(
                textStringResId = R.string.title,
                actionStringResId = R.string.action
            )
        ) {}
    }

    @Composable
    private fun GenericInfoBarWithStringResourceAndArgsForTextAndAction() {
        InfoBar(
            offeredMessage = InfoBarMessage(
                textStringResId = R.string.title_with_args,
                textStringResArgs = arrayOf(17, 1, 1),
                actionStringResId = R.string.action_with_args,
                actionStringResArgs = arrayOf(1)
            )
        ) {}
    }

    @Composable
    private fun GenericInfoBarWithNoTitleOrAction() {
        InfoBar(offeredMessage = InfoBarMessage()) {}
    }

    @Composable
    fun GenericInfoBarWithFadeEffectOnly() {
        InfoBar(
            offeredMessage = InfoBarMessage(EXAMPLE_SHORT_STRING),
            fadeEffect = true,
            scaleEffect = false,
            slideEffect = InfoBarSlideEffect.NONE
        ) {}
    }

    @Composable
    fun GenericInfoBarWithScaleEffectOnly() {
        InfoBar(
            offeredMessage = InfoBarMessage(EXAMPLE_SHORT_STRING),
            fadeEffect = false,
            scaleEffect = true,
            slideEffect = InfoBarSlideEffect.NONE
        ) {}
    }

    @Composable
    fun GenericInfoBarWithSlideFromTopEffectOnly() {
        InfoBar(
            offeredMessage = InfoBarMessage(EXAMPLE_SHORT_STRING),
            fadeEffect = false,
            scaleEffect = false,
            slideEffect = InfoBarSlideEffect.FROM_TOP
        ) {}
    }

    @Composable
    fun GenericInfoBarWithSlideFromBottomEffectOnly() {
        InfoBar(
            offeredMessage = InfoBarMessage(EXAMPLE_SHORT_STRING),
            fadeEffect = false,
            scaleEffect = false,
            slideEffect = InfoBarSlideEffect.FROM_BOTTOM
        ) {}
    }

    @Composable
    fun GenericInfoBarWithFadeAndScaleEffects(offeredMessage: InfoBarMessage = InfoBarMessage(EXAMPLE_SHORT_STRING)) {
        InfoBar(
            offeredMessage = offeredMessage,
            fadeEffect = true,
            scaleEffect = true,
            slideEffect = InfoBarSlideEffect.NONE
        ) {}
    }

    @Composable
    fun GenericInfoBarWithAllEffects() {
        InfoBar(
            offeredMessage = InfoBarMessage(EXAMPLE_SHORT_STRING),
            fadeEffect = true,
            scaleEffect = true,
            slideEffect = InfoBarSlideEffect.FROM_TOP
        ) {}
    }
    
    @Composable
    fun CustomInfoBarComposable() {
        val message = CustomInfoBarMessage(
            "Custom InfoBar",
            gradientStartColor = Color.Black,
            gradientEndColor = Color.Yellow
        )
        InfoBar(offeredMessage = message, content = customContent) {}
    }

    // Private

    private fun waitForInfoBar() {
        rule.waitUntil {
            rule.onAllNodesWithContentDescription(INFO_BAR_CONTENT_DESCRIPTION)
                .fetchSemanticsNodes().size == 1
        }
    }

    private fun setContent(content: @Composable () -> Unit) {
        rule.setContent {
            MaterialTheme {
                content()
            }
        }
    }

    private fun checkAgainstScreenshot(goldenName: String) {
        assertScreenshotMatchesGolden(
            folderName = TEST_TAG_NAME,
            goldenName = goldenName,
            node = rule.onRoot()
        )
    }

    private fun advanceTimeBy(millis: Int) {
        // Workaround until https://issuetracker.google.com/issues/196060592 is fixed
        repeat(millis / 16) {
            rule.mainClock.advanceTimeByFrame()
        }
    }

    private fun debugLog() {
        rule.onRoot().printToLog("InfoBarKtTest")
    }

    // Companion

    companion object {
        const val TEST_TAG_NAME = "InfoBar"
        val EXAMPLE_SHORT_STRING = LoremIpsum(3).values.first()
        val EXAMPLE_LONG_STRING = LoremIpsum(20).values.first()
        const val EXAMPLE_ACTION = "Action"

        @BeforeClass
        @JvmStatic
        fun clearExistingImagesBeforeStart() {
            clearExistingImages(TEST_TAG_NAME)
        }
    }
}