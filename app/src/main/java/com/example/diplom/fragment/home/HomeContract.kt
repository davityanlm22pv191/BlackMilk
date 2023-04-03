package com.example.diplom.fragment.home

import android.graphics.Bitmap

interface HomeContract {

	fun navigateToChooseImageFromLink(imageNumber: Int)

	fun setImageOneFromGallery()

	fun setImageTwoFromGallery()
}