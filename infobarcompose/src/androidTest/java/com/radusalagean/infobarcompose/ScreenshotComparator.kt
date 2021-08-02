package com.radusalagean.infobarcompose

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.captureToImage
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.io.FileOutputStream

/**
 * Simple on-device screenshot comparator that uses golden images present in
 * `androidTest/assets`.
 *
 * Original file written by Jose Alcerreca (Google):
 * https://github.com/googlecodelabs/android-compose-codelabs/blob/main/TestingCodelab/app/src/androidTest/java/com/example/compose/rally/ScreenshotComparator.kt
 *
 * Additional changes written by Mark Allison (StylingAndroid.com):
 * https://github.com/StylingAndroid/ComposeStrikethru/blob/snapshot_testing/app/src/androidTest/java/com/stylingandroid/compose/strikethru/ScreenshotComparator.kt
 *
 * Screenshots are saved on device in `/data/data/{package}/files`.
 *
 * Screenshot names will have the bitmap size included. This allows for different golden images
 * to be used for different screen densities. You will need to ensure that golden images with the
 * appropriate size in the name is included for all supported densities.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun assertScreenshotMatchesGolden(
    folderName: String,
    goldenName: String,
    node: SemanticsNodeInteraction
) {
    val bitmap = node.captureToImage().asAndroidBitmap()

    // Save screenshot to file for debugging
    saveScreenshot(
        folderName,
        "${goldenName}_${bitmap.width}x${bitmap.height}",
        bitmap
    )
    val golden = InstrumentationRegistry.getInstrumentation()
        .context.resources.assets.open(
            "$folderName/${goldenName}_${bitmap.width}x${bitmap.height}.webp"
        )
        .use { BitmapFactory.decodeStream(it) }

    golden.compare(bitmap)
}

private fun saveScreenshot(folderName: String, filename: String, bmp: Bitmap) {
    val path = File(InstrumentationRegistry.getInstrumentation().targetContext.filesDir, folderName)
    if (!path.exists()) {
        path.mkdirs()
    }
    FileOutputStream("$path/$filename.webp").use { out ->
        bmp.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, out)
    }
    println("Saved screenshot to $path/$filename.webp")
}

private fun Bitmap.compare(other: Bitmap) {
    if (this.width != other.width || this.height != other.height) {
        throw AssertionError("Size of screenshot does not match golden file (check device density)")
    }
    // Compare row by row to save memory on device
    val row1 = IntArray(width)
    val row2 = IntArray(width)
    for (column in 0 until height) {
        // Read one row per bitmap and compare
        this.getRow(row1, column)
        other.getRow(row2, column)
        if (!row1.contentEquals(row2)) {
            throw AssertionError("Sizes match but bitmap content has differences (column: $column)")
        }
    }
}

private fun Bitmap.getRow(pixels: IntArray, column: Int) {
    this.getPixels(pixels, 0, width, 0, column, width, 1)
}

internal fun clearExistingImages(folderName: String) {
    val path = File(InstrumentationRegistry.getInstrumentation().targetContext.filesDir, folderName)
    path.deleteRecursively()
}