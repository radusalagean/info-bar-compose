# InfoBar Compose

An **Android Jetpack Compose library** for displaying **on-screen messages**. As opposed to the built-in `Snackbar` from the Compose Material library, the `InfoBar` can be properly displayed **without additional requirements**, like `Scaffold`, `SnackbarHost` / `SnackbarHostState`, or manually starting new coroutines to show the on-screen message.

Although the **InfoBar** composable is inspired by the Snackbar, it does not aim to entirely copy its design or behavior.

![generic_info_bar_with_string_title_no_wrap_1080x181](https://user-images.githubusercontent.com/11408459/129700937-7c048349-7b56-46bc-a6f7-1781004674d1.png)
![generic_info_bar_with_string_title_and_action_1080x181](https://user-images.githubusercontent.com/11408459/129700942-ba4e8081-8995-4cf0-a007-4daf970a6f5f.png)


## Usage

Include the library in your module-level `build.gradle` file:
```
repositories {
    mavenCentral()
}

dependencies {
    implementation '' // TODO Radu: Add dependency name after publishing to MavenCentral
}
```

The **simplest configuration** of an `InfoBar` is showcased below:

```kotlin
var message: InfoBarMessage? by remember { mutableStateOf(null) }

// Assign the message on an event callback (button click, download complete, message received, etc.):
// message = InfoBarMessage(text = "Example message")

InfoBar(offeredMessage = message) {
    // ⚠️ Important step: We are nulling out the message in the trailing lambda (onDismiss function)
    message = null
}
```

A **complete working example**:
```kotlin
setContent {
    YourAppTheme {
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            var message: InfoBarMessage? by remember { mutableStateOf(null) }
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = { message = InfoBarMessage(text = "Example message") }
            ) {
                Text("Show message")
            }
            InfoBar(offeredMessage = message) {
                message = null
            }
        }
    }
}
```
![simple-usage](https://user-images.githubusercontent.com/11408459/129697170-36e7f019-1189-493e-ad99-0d753da1f357.gif)

### Configuration parameters
`InfoBar` composable:
| S | G |
| --- | --- |
| **Standard** InfoBar | **Generic** InfoBar |

| Parameter | Description | Type | Default value | S | G |
| --- | --- | --- | --- | --- | --- |
| `modifier` | Modifier to be applied to the InfoBar surface | `Modifier` | `Modifier` | ✔️ | ✔️ |
| `offeredMessage` | `InfoBarMessage` instance describing the message that should currently be displayed | `InfoBarMessage?` | ❌ | ✔️ | ✔️ |
| `elevation` | Elevation to be applied to the InfoBar surface | `Dp` | `6.dp` | ✔️ | ✔️ |
| `shape` | Shape to be applied to the InfoBar surface | `Shape` | `MaterialTheme.shapes.small` | ✔️ | ✔️ |
| `backgroundColor` | Background color to be applied to the InfoBar surface | `Color?` | `null` | ✔️ | ✔️ |
| `content` | The content composable to use in the `InfoBar` surface | `@Composable (T) -> Unit` | ❌ | ❌ | ✔️ |
| `textColor` | Color for the message text | `Color?` | `null` | ✔️ | ❌ |
| `textFontSize` | Font size for the message text | `TextUnit` | `TextUnit.Unspecified` | ✔️ | ❌ |
| `textFontStyle` | Font style for the message text | `FontStyle?` | `null` | ✔️ | ❌ |
| `textFontWeight` | Font weight for the message text | `FontWeight?` | `null` | ✔️ | ❌ |
| `textFontFamily` | Font family for the message text | `FontFamily?` | `null` | ✔️ | ❌ |
| `textLetterSpacing` | Letter spacing for the message text | `TextUnit` | `TextUnit.Unspecified` | ✔️ | ❌ |
| `textDecoration` | Decoration for the message text | `TextDecoration?` | `null` | ✔️ | ❌ |
| `textAlign` | Alignment for the message text | `TextAlign?` | `null` | ✔️ | ❌ |
| `textLineHeight` | Line height for the message text | `TextUnit` | `TextUnit.Unspecified` | ✔️ | ❌ |
| `textMaxLines` | Maximum number of lines for the message text | `Int` | `5` | ✔️ | ❌ |
| `textStyle` | Style for the message text | `TextStyle` | `LocalTextStyle.current` | ✔️ | ❌ |
| `actionColor` | Color for the action button | `Color?` | `null` | ✔️ | ❌ |
| `fadeEffect` | Use fading effect when the message appears and disappears? (controls the `alpha` property) | `Boolean` | `true` | ✔️ | ✔️ |
| `fadeEffectEasing` | Easing style of the fade effect | `InfoBarEasing` | `InfoBarEasing(LinearEasing)` | ✔️ | ✔️ |
| `scaleEffect` | Use scaling effect when the message appears and disappears? (controls the `scaleX` / `scaleY` properties) | `Boolean` | `true` | ✔️ | ✔️ |
| `scaleEffectEasing` | Easing style of the scale effect | `InfoBarEasing` | `InfoBarEasing(FastOutSlowInEasing)` | ✔️ | ✔️ |
| `slideEffect` | Which sliding effect to use when the message appears and disappears? (controls the `translationY` property) | `InfoBarSlideEffect` | `InfoBarSlideEffect.NONE` | ✔️ | ✔️ |
| `slideEffectEasing` | Easing style of the slide effect | `InfoBarEasing` | `InfoBarEasing(FastOutSlowInEasing)` | ✔️ | ✔️ |
| `enterTransitionMillis` | Enter animation duration in milliseconds | `Int` | `150` | ✔️ | ✔️ |
| `exitTransitionMillis` | Exit animation duration in milliseconds | `Int` | `75` | ✔️ | ✔️ |
| `wrapInsideExpandedBox` | Maintain the shadow of the InfoBar even when animating the `alpha` property, by wrapping the `InfoBar` content inside a `Box` layout that fills the maximum available space. The `alpha` property is then animated on the outer `Box` instead of the `InfoBar` surface, thus not clipping the shadow when `alpha` is less than `1f`. **Note: Any modifier you pass from the outside is applied to the `InfoBar` surface, not the outer `Box` layout!** | `Boolean` | `true` | ✔️ | ✔️ |
| `onDismiss` | Function which is called when the InfoBar is either timed out or dismissed by the user. **Don't forget to always null out the `InfoBarMessage` instance here!** (see usage example from above) |  `() -> Unit` | ❌ | ✔️ | ✔️ |
