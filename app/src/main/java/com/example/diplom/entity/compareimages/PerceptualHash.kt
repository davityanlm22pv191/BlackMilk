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
		const val VERY_STRONG = 256

		const val COLOR_FILTER_COUNT = 3

		const val HASHES_NOT_COMPARE = 30
	}

	// region ================ Public ====================

	fun convertBitmapToBinaryHashString(imageBitmap: Bitmap, accuracy: Int): String {
		if (imageBitmap.width <= 0 || imageBitmap.height <= 0) {
			return "Bitmap is empty"
		}
		if (accuracy > imageBitmap.width || accuracy > imageBitmap.height) {
			return "Accuracy can't be greater than image dimension"
		}

		val pixelArray = IntArray(imageBitmap.width * imageBitmap.height)
		imageBitmap.getPixels(
			pixelArray,
			0,
			imageBitmap.width,
			0,
			0,
			imageBitmap.width,
			imageBitmap.height
		)

		val averagePixel = pixelArray.average().toInt()

		var binaryHash = BigInteger.ZERO
		var bitPosition = BigInteger.ONE

		for (y in 0 until accuracy) {
			for (x in 0 until accuracy) {
				val pixel =
					pixelArray[(x * imageBitmap.width / accuracy) + (y * imageBitmap.height / accuracy) * imageBitmap.width]
				if (pixel >= averagePixel) {
					binaryHash = binaryHash.or(bitPosition)
				}
				bitPosition = bitPosition.shiftLeft(1)
				if (y == accuracy - 1 && x == accuracy - 1) {
					binaryHash =
						binaryHash.setBit(accuracy * accuracy - 1) // устанавливаем последний бит в 1
					break
				}
			}
		}

		return String.format("%064d", BigInteger(binaryHash.toString(2)))
	}

	fun convertBitmapToBinaryHash(imageBitmap: Bitmap, accuracy: Int): BigInteger? {
		if (imageBitmap.width <= 0 || imageBitmap.height <= 0) {
			return null
		}
		if (accuracy > imageBitmap.width || accuracy > imageBitmap.height) {
			return null
		}

		val pixelArray = IntArray(imageBitmap.width * imageBitmap.height)
		imageBitmap.getPixels(
			pixelArray,
			0,
			imageBitmap.width,
			0,
			0,
			imageBitmap.width,
			imageBitmap.height
		)

		val averagePixel = pixelArray.average().toInt()

		var binaryHash = BigInteger.ZERO
		var bitPosition = BigInteger.ONE

		for (y in 0 until accuracy) {
			for (x in 0 until accuracy) {
				val pixel =
					pixelArray[(x * imageBitmap.width / accuracy) + (y * imageBitmap.height / accuracy) * imageBitmap.width]
				if (pixel >= averagePixel) {
					binaryHash = binaryHash.or(bitPosition)
				}
				bitPosition = bitPosition.shiftLeft(1)
				if (y == accuracy - 1 && x == accuracy - 1) {
					return binaryHash.setBit(accuracy * accuracy - 1) // устанавливаем 64-й бит в 1
				}
			}
		}
		val paddingBits = accuracy * accuracy - bitPosition.bitLength()
		if (paddingBits > 0) {
			binaryHash = binaryHash.shiftLeft(paddingBits).or(BigInteger.ZERO)
		}
		return binaryHash
	}

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

	private fun hammingDistance(hash1: BigInteger, hash2: BigInteger): Int {
		if (hash1.bitLength() != hash2.bitLength()) {
			return HASHES_NOT_COMPARE
		}

		var distance = 0
		var diffBits = hash1.xor(hash2)

		while (diffBits != BigInteger.ZERO) {
			if (diffBits.and(BigInteger.ONE) != BigInteger.ZERO) {
				distance++
			}
			diffBits = diffBits.shiftRight(1)
		}

		return distance
	}

	// endregion
}