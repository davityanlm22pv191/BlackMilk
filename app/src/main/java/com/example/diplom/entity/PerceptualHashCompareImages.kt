package com.example.diplom.entity

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

class PerceptualHashCompareImages {

	private companion object {
		const val SCALED_BITMAP_WIDTH = 8
		const val SCALED_BITMAP_HEIGHT = 8
	}

	// region ================ Public ====================

	fun compare(imageBitmapOne: Bitmap, imageBitmapTwo: Bitmap) {
		val scaledBitmapOne: Bitmap = Bitmap.createScaledBitmap(
			imageBitmapOne,
			SCALED_BITMAP_WIDTH,
			SCALED_BITMAP_HEIGHT,
			true
		)

		val scaledBitmapTwo: Bitmap = Bitmap.createScaledBitmap(
			imageBitmapTwo,
			SCALED_BITMAP_WIDTH,
			SCALED_BITMAP_HEIGHT,
			true
		)

		val grayScaleImageOne: Bitmap = this.toGrayScale(scaledBitmapOne)
		val grayScaleImageTwo: Bitmap = this.toGrayScale(scaledBitmapTwo)

	}

	fun getScaledBitmap(imageBitmap: Bitmap): Bitmap {
		return Bitmap.createScaledBitmap(
			imageBitmap,
			SCALED_BITMAP_WIDTH,
			SCALED_BITMAP_HEIGHT,
			true
		)
	}

	fun getGrayScaleBitmap(imageBitmap: Bitmap): Bitmap {
		return this.toGrayScale(imageBitmap)
	}

	// endregion

	// region ================ Private ====================

	private fun toGrayScale(imageBitmap: Bitmap): Bitmap {
		val width = imageBitmap.width
		val height = imageBitmap.height

		val grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

		val paint = Paint()
		val colorMatrix = ColorMatrix()
		colorMatrix.setSaturation(0f)
		val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
		paint.colorFilter = colorMatrixFilter

		return grayBitmap
	}

	// endregion
}