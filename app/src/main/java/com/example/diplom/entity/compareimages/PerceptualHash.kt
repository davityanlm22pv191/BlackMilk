package com.example.diplom.entity.compareimages

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.os.Build.VERSION_CODES
import java.math.BigInteger

class PerceptualHash {

	private companion object {
		const val VERY_WEAK = 16
		const val WEAK = 32
		const val NORMAL = 64
		const val STRONG = 128
		const val VERY_STRONG = 8

		const val COLOR_FILTER_COUNT = 3
	}

	// region ================ Public ====================

//	fun convertBitmapToBinaryHash(imageBitmap: Bitmap, accuracy: Int): BigInteger {
//		require(imageBitmap.width > 0 && imageBitmap.height > 0) { "Bitmap is empty" }
//		require(accuracy <= imageBitmap.width && accuracy <= imageBitmap.height) { "Accuracy can't be greater than image dimension" }
//
//		val pixelArray = IntArray(imageBitmap.width * imageBitmap.height)
//		imageBitmap.getPixels(pixelArray, 0, imageBitmap.width, 0, 0, imageBitmap.width, imageBitmap.height)
//
//		val averagePixel = pixelArray.average().toInt()
//
//		var binaryHash = BigInteger.ZERO
//		var bitPosition = BigInteger.ONE
//
//		for (y in 0 until accuracy) {
//			for (x in 0 until accuracy) {
//				val pixel = pixelArray[(x * imageBitmap.width / accuracy) + (y * imageBitmap.height / accuracy) * imageBitmap.width]
//				if (pixel >= averagePixel) {
//					binaryHash = binaryHash.or(bitPosition)
//
//				}
//				bitPosition = bitPosition.shiftLeft(1)
//			}
//		}
//
//		return binaryHash
//	}
//	fun convertBitmapToBinary(image: Bitmap, threshold: Int, accuracy: Int,): Long {
//		var hash: Long = 0
//		val pixels = LongArray(image.width * image.height)
//		for (y in 0 until image.height) {
//			for (x in 0 until image.width) {
//				val pixel = image.getPixel(x, y)
//				val alpha = Color.alpha(pixel)
//				val red = Color.red(pixel)
//				val green = Color.green(pixel)
//				val blue = Color.blue(pixel)
//				val gray = (0.2989 * red + 0.587 * green + 0.114 * blue).toInt()
//				pixels[y * image.width + x] =
//					if (gray > threshold) ((alpha ushl 24) or 1).toLong() else ((alpha shl 24) or 0).toLong()
//			}
//		}
//		for (i in 0 until accuracy ) {
//			hash = hash or (pixels[i] shl i)
//		}
//		return hash
//	}

	fun getAccuracyByApiLevel(): Int {
		return when (Build.VERSION.SDK_INT) {
			in VERSION_CODES.BASE..VERSION_CODES.M -> {
				VERY_WEAK
			}
			VERSION_CODES.N, VERSION_CODES.N_MR1 -> {
				WEAK
			}
			VERSION_CODES.O, VERSION_CODES.O_MR1 -> {
				NORMAL
			}
			VERSION_CODES.Q, VERSION_CODES.R -> {
				STRONG
			}
			VERSION_CODES.S, VERSION_CODES.S_V2, VERSION_CODES.TIRAMISU -> {
				VERY_STRONG
			}
			else -> STRONG
		}
	}

	fun getScaledBitmap(imageBitmap: Bitmap): Bitmap {
		val accuracy: Int = getAccuracyByApiLevel()
		return Bitmap.createScaledBitmap(
			imageBitmap,
			accuracy,
			accuracy,
			true
		)
	}

	fun getGrayScaleBitmap(imageBitmap: Bitmap): Bitmap {
		return this.convertToGrayScale(imageBitmap)
	}

	fun getPixelCount(imageBitmap: Bitmap): Int {
		return imageBitmap.width * imageBitmap.height
	}

	fun getPixelSum(imageBitmap: Bitmap): Int {
		var sum = 0
		for (x in 0 until imageBitmap.width) {
			for (y in 0 until imageBitmap.height) {
				val currentPixel = imageBitmap.getPixel(x, y)
				val red = Color.red(currentPixel)
				val green = Color.green(currentPixel)
				val blue = Color.blue(currentPixel)
				sum += (red + green + blue) / COLOR_FILTER_COUNT
			}
		}
		return sum
	}

	fun getAverageValueOfPixels(imageBitmap: Bitmap): Int {
		return getPixelSum(imageBitmap) / getPixelCount(imageBitmap)
	}

	// endregion

	// region ================ Private ====================

	private fun convertToGrayScale(imageBitmap: Bitmap): Bitmap {
		val width = imageBitmap.width
		val height = imageBitmap.height

		val grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

		val canvas = Canvas(grayBitmap)
		val paint = Paint()
		val colorMatrix = ColorMatrix()
		colorMatrix.setSaturation(0f)
		val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
		paint.colorFilter = colorMatrixFilter
		canvas.drawBitmap(imageBitmap, 0f, 0f, paint)

		return grayBitmap
	}

	// endregion
}