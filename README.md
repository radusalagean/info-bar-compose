# InfoBar Compose

An **Android Jetpack Compose library** for displaying **on-screen messages**. As opposed to the built-in `Snackbar` from the Compose Material library, the `InfoBar` can be properly displayed **without additional requirements**, like `Scaffold`, `SnackbarHost` / `SnackbarHostState`, or manually starting new coroutines to show the on-screen message.

Although the **InfoBar** composable is inspired by the Snackbar, it does not aim to fully copy its design or behavior.

![generic_info_bar_with_string_title_1080x173](https://user-images.githubusercontent.com/11408459/127895397-868d0337-9419-40fa-b6f4-4b28b4e455d8.png)

![generic_info_bar_with_string_title_and_action_1080x211](https://user-images.githubusercontent.com/11408459/127895396-f6509ce8-6f1f-461b-91be-5265f64f6b65.png)


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
    // We are nulling out the message in the trailing lambda (onMessageTimeout function)
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
![generic-infobar](https://user-images.githubusercontent.com/11408459/127901254-afbb1e68-01e7-45ed-bbea-d8b9f9edee7e.gif)
