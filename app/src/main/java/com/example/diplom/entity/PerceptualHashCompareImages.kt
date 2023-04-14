package com.example.diplom.entity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.example.diplom.helpers.DimenHelper

class PerceptualHashCompareImages {

	private companion object {
		const val COLOR_FILTER_COUNT = 3
	}

	// region ================ Public ====================

	fun getScaledBitmap(imageBitmap: Bitmap): Bitmap {
		return Bitmap.createScaledBitmap(
			imageBitmap,
			DimenHelper.DIMEN_8_DP,
			DimenHelper.DIMEN_8_DP,
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