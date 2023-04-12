package com.example.diplom.entity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

class PerceptualHashCompareImages {

	private companion object {
		const val SCALED_BITMAP_WIDTH = 8
		const val SCALED_BITMAP_HEIGHT = 8
	}

	// region ================ Public ====================

	fun getScaledBitmap(imageBitmap: Bitmap): Bitmap {
		return Bitmap.createScaledBitmap(
			imageBitmap,
			SCALED_BITMAP_WIDTH,
			SCALED_BITMAP_HEIGHT,
			true
		)
	}

	fun getGrayScaleBitmap(imageBitmap: Bitmap): Bitmap {
		return this.convertToGrayScale(imageBitmap)
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