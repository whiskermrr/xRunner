package com.whisker.mrr.xrunner.presentation.layout

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.squareup.picasso.Transformation

class BlurTransformation(
    private val context: Context,
    private val radius: Int,
    private val isFast: Boolean = true
) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        return if(isFast) fastBlurAlgorithm(source) else rawBlurAlgorithm(source)
    }

    override fun key(): String {
        return BlurTransformation::class.java.simpleName + " " + radius.toString()
    }

    private fun rawBlurAlgorithm(source: Bitmap) : Bitmap {
        val imageBitmap = source.copy(source.config, true)
        val width = imageBitmap.width
        val height = imageBitmap.height
        val pixels = IntArray(width * height)
        val newPixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        source.recycle()

        val divider = (2 * radius + 1) * (2 * radius + 1)

        for(i in 0 until height) {
            for(j in 0 until width) {
                var sumRed = 0
                var sumGreen = 0
                var sumBlue = 0
                for(m in -radius..radius) {
                    for(n in -radius..radius) {
                        val pixelX = j + m
                        val pixelY = i + n
                        val pixelIndex = width * pixelY + pixelX
                        if((pixelIndex >= 0 && pixelIndex < pixels.size)) {
                            val pixel = pixels[pixelIndex]
                            sumRed += pixel and 0xff0000 shr 16
                            sumGreen += pixel and 0x00ff00 shr 8
                            sumBlue += pixel and 0x0000ff
                        }
                    }
                }
                newPixels[width * i + j] = -0x1000000 or ((sumRed / divider) shl 16) or ((sumGreen / divider) shl 8) or (sumBlue / divider)
            }
        }
        imageBitmap.setPixels(newPixels, 0, width, 0, 0, width, height)
        return imageBitmap
    }

    private fun fastBlurAlgorithm(source: Bitmap) : Bitmap {
        val renderScript = RenderScript.create(context)
        val tmpInput = Allocation.createFromBitmap(renderScript, source)
        val tmpOutput = Allocation.createTyped(renderScript, tmpInput.type)
        val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        blurScript.setRadius(radius.toFloat())
        blurScript.setInput(tmpInput)
        blurScript.forEach(tmpOutput)
        tmpOutput.copyTo(source)

        return source
    }
}