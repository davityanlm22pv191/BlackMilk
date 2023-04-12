package com.example.diplom.fragment.showcompareprocess

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.diplom.R
import com.example.diplom.entity.PerceptualHashCompareImages
import com.example.diplom.fragment.showcompareprocess.model.ShowCompareProcessParams

class ShowCompareProcessFragment(
	private val showCompareProcessParams: ShowCompareProcessParams,
) : Fragment(R.layout.fragment_show_compare_proccess), ShowCompareProcessContract {

	/** Init class for alg process */
	private val perceptualHashCompareImages = PerceptualHashCompareImages()

	/** Step first */
	private val srcImageOne: Bitmap = showCompareProcessParams.imageBitmapOne
	private val srcImageTwo: Bitmap = showCompareProcessParams.imageBitmapTwo

	/** Step second */
	private var scaledImageOne: Bitmap? = null
	private var scaledImageTwo: Bitmap? = null

	/** Step third */
	private var grayScaleImageOne: Bitmap? = null
	private var grayScaleImageTwo: Bitmap? = null

	//region ==================== Object creation ====================

	companion object {
		fun newInstance(
			showCompareProcessParams: ShowCompareProcessParams,
		): ShowCompareProcessFragment {
			val args = Bundle()
			val fragment = ShowCompareProcessFragment(showCompareProcessParams)
			fragment.arguments = args
			return fragment
		}
	}

	// endregion

	// region ==================== ShowCompareProcessContract ====================

	override fun close() {
		parentFragmentManager.beginTransaction()
			.remove(this)
			.commit()
	}

	override fun onBackClicked() {
		requireActivity().onBackPressedDispatcher.addCallback(
			viewLifecycleOwner,
			object : OnBackPressedCallback(true) {
				override fun handleOnBackPressed() {
					close()
				}
			})
	}

	// endregion

	// region ==================== Lifecycle ====================

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_show_compare_proccess, container, false)
		initUI(view)

		return view
	}

	// endregion

	//region ==================== Internal ====================

	private fun initUI(view: View) {
		view.apply {
			setupBackActions(this)

			/** Step first */
			setImagesSources(this)

			/** Step second */
			setScaledImages(this)

			/** Step third */
			setGrayScaleImages(this)
		}
	}

	private fun setGrayScaleImages(view: View) {
		scaledImageOne?.let {
			grayScaleImageOne = perceptualHashCompareImages.getGrayScaleBitmap(it)
		}
		scaledImageTwo?.let {
			grayScaleImageTwo = perceptualHashCompareImages.getGrayScaleBitmap(it)
		}
		view.findViewById<ImageView>(R.id.ivGrayScaleImageOne).setImageBitmap(grayScaleImageOne)
		view.findViewById<ImageView>(R.id.ivGrayScaleImageTwo).setImageBitmap(grayScaleImageTwo)
	}

	private fun setScaledImages(view: View) {
		scaledImageOne = perceptualHashCompareImages.getScaledBitmap(srcImageOne)
		scaledImageTwo = perceptualHashCompareImages.getScaledBitmap(srcImageTwo)
		view.findViewById<ImageView>(R.id.ivScaledImageOne)
			.setImageBitmap(scaledImageOne)
		view.findViewById<ImageView>(R.id.ivScaledImageTwo)
			.setImageBitmap(scaledImageTwo)
	}

	private fun setupBackActions(view: View) {
		view.findViewById<ImageView>(R.id.ivArrowBack).setOnClickListener { close() }
		onBackClicked()
	}

	private fun setImagesSources(view: View) {
		view.findViewById<ImageView>(R.id.ivSourceImageOne)
			.setImageBitmap(showCompareProcessParams.imageBitmapOne)
		view.findViewById<ImageView>(R.id.ivSourceImageTwo)
			.setImageBitmap(showCompareProcessParams.imageBitmapTwo)
	}

	// endregion
}