package com.example.diplom.fragment.home.callback

import android.graphics.Bitmap

interface HomeCallback {
	fun onDataReceived(bitmapImage: Bitmap, link: String, imageNumber: Int)
}