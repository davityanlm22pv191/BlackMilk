package com.example.diplom.fragment.home

interface HomeContract {

	fun navigateToChooseImageFromLink(imageNumber: Int)

	fun setImageOneFromGallery()

	fun setImageTwoFromGallery()

	fun isAllImageLoaded(): Boolean

	fun checkToReadyCompare()
}