package com.example.diplom.fragment.home.callback

import android.graphics.Bitmap

interface HomeCallback {
	fun setImageFromLink(bitmapImage: Bitmap, link: String, imageNumber: Int)

	fun setCompareResult(isDuplicate: Boolean)
}