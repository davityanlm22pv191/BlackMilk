package com.example.diplom.fragment.showcompareprocess

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.diplom.R
import com.example.diplom.entity.compareimages.PerceptualHash
import com.example.diplom.fragment.showcompareprocess.model.ShowCompareProcessParams
import java.math.BigInteger

class ShowCompareProcessFragment(
	private val showCompareProcessParams: ShowCompareProcessParams,
) : Fragment(R.layout.fragment_show_compare_proccess), ShowCompareProcessContract {

	/** Init class for alg process */
	private val perceptualHash = PerceptualHash()

	/** Step first */
	private val srcImageOne: Bitmap = showCompareProcessParams.imageBitmapOne
	private val srcImageTwo: Bitmap = showCompareProcessParams.imageBitmapTwo

	/** Step second */
	private var scaledImageOne: Bitmap? = null
	private var scaledImageTwo: Bitmap? = null

	/** Step third */
	private var grayScaleImageOne: Bitmap? = null
	private var grayScaleImageTwo: Bitmap? = null

	/** Step fourth */
	private var averagePixelImageOne: Int? = null
	private var averagePixelImageTwo: Int? = null

	/** Step fifth */
	private var hashImageOne: BigInteger? = null
	private var hashImageTwo: BigInteger? = null

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

			/** Step Fourth */
			setPixelsInfo(this)

			/** Step fifth */
			setCreatingHashResult(this)

			/** Step sixth */
			setResult(this)

		}
	}

	private fun setResult(view: View) {
		val ivCompareResult = view.findViewById<ImageView>(R.id.ivCompareResult)
		hashImageOne?.let { hashOne ->
			hashImageTwo?.let { hashTwo ->
				if (hashOne.bitLength() != hashTwo.bitLength()) {
					ivCompareResult.setImageResource(R.drawable.ic_not_equal_red)
				} else {
					ivCompareResult.setImageResource(R.drawable.ic_equal_green)
				}
			}
		}
	}

	private fun setCreatingHashResult(view: View) {
		val accuracyByApiLevel = perceptualHash.getAccuracyByApiLevel()
		view.apply {
			grayScaleImageOne?.let { bitmap ->
				hashImageOne = perceptualHash.convertBitmapToBinaryHash(
					bitmap,
					accuracyByApiLevel
				)
			}
			grayScaleImageTwo?.let { bitmap ->
				hashImageTwo = perceptualHash.convertBitmapToBinaryHash(
					bitmap,
					accuracyByApiLevel
				)
			}
			val tvSuccessOrFailImageOne = findViewById<TextView>(R.id.tvSuccessOrFailImageOne)
			val ivSuccessOrFailImageOne = findViewById<ImageView>(R.id.ivSuccessOrFailImageOne)
			val tvHashLengthImageOne = findViewById<TextView>(R.id.tvHashLengthImageOne)
			val tvSuccessOrFailImageTwo = findViewById<TextView>(R.id.tvSuccessOrFailImageTwo)
			val ivSuccessOrFailImageTwo = findViewById<ImageView>(R.id.ivSuccessOrFailImageTwo)
			val tvHashLengthImageTwo = findViewById<TextView>(R.id.tvHashLengthImageTwo)

			if (hashImageOne != null) {
				tvHashLengthImageOne.text = resources.getString(
					R.string.show_compare_process_hash_length,
					hashImageOne?.bitLength()?.toString()
				)
				tvSuccessOrFailImageOne.text =
					resources.getString(R.string.show_compare_process_creating_perceptive_hash_code_success)
				tvSuccessOrFailImageOne.setTextAppearance(R.style.S18Green46)
				ivSuccessOrFailImageOne.setImageResource(R.drawable.ic_success)
			} else {
				tvSuccessOrFailImageOne.text =
					resources.getString(R.string.show_compare_process_creating_perceptive_hash_code_failed)
				tvSuccessOrFailImageOne.setTextAppearance(R.style.S18Red46)
				ivSuccessOrFailImageOne.setImageResource(R.drawable.ic_failed)
			}

			if (hashImageTwo != null) {
				tvHashLengthImageTwo.text = resources.getString(
					R.string.show_compare_process_hash_length,
					hashImageTwo?.bitLength()?.toString()
				)
				tvSuccessOrFailImageTwo.text =
					resources.getString(R.string.show_compare_process_creating_perceptive_hash_code_success)
				tvSuccessOrFailImageTwo.setTextAppearance(R.style.S18Green46)
				ivSuccessOrFailImageTwo.setImageResource(R.drawable.ic_success)
			} else {
				tvSuccessOrFailImageTwo.text =
					resources.getString(R.string.show_compare_process_creating_perceptive_hash_code_failed)
				tvSuccessOrFailImageTwo.setTextAppearance(R.style.S18Red46)
				ivSuccessOrFailImageTwo.setImageResource(R.drawable.ic_failed)
			}
		}
	}

	private fun setPixelsInfo(view: View) {
		view.apply {
			grayScaleImageOne?.let {
				findViewById<TextView>(R.id.tvPixelCountImageOne).text = resources.getString(
					R.string.show_compare_process_pixels_info_pixels_count,
					perceptualHash.getPixelCount(it).toString()
				)
				findViewById<TextView>(R.id.tvPixelsSumImageOne).text = resources.getString(
					R.string.show_compare_process_pixels_info_pixels_sum,
					perceptualHash.getPixelSum(it).toString()
				)
				averagePixelImageOne = perceptualHash.getAverageValueOfPixels(it)
				findViewById<TextView>(R.id.tvAveragePixelImageOne).text = resources.getString(
					R.string.show_compare_process_pixels_info_average_pixel,
					averagePixelImageOne.toString()
				)
			}
			grayScaleImageTwo?.let {
				findViewById<TextView>(R.id.tvPixelCountImageTwo).text = resources.getString(
					R.string.show_compare_process_pixels_info_pixels_count,
					perceptualHash.getPixelCount(it).toString()
				)
				findViewById<TextView>(R.id.tvPixelsSumImageTwo).text = resources.getString(
					R.string.show_compare_process_pixels_info_pixels_sum,
					perceptualHash.getPixelSum(it).toString()
				)
				averagePixelImageTwo = perceptualHash.getAverageValueOfPixels(it)
				findViewById<TextView>(R.id.tvAveragePixelImageTwo).text = resources.getString(
					R.string.show_compare_process_pixels_info_average_pixel,
					averagePixelImageTwo.toString()
				)
			}
		}
	}

	private fun setGrayScaleImages(view: View) {
		scaledImageOne?.let {
			grayScaleImageOne = perceptualHash.getGrayScaleBitmap(it)
		}
		scaledImageTwo?.let {
			grayScaleImageTwo = perceptualHash.getGrayScaleBitmap(it)
		}
		view.findViewById<ImageView>(R.id.ivGrayScaleImageOne).setImageBitmap(grayScaleImageOne)
		view.findViewById<ImageView>(R.id.ivGrayScaleImageTwo).setImageBitmap(grayScaleImageTwo)
	}

	private fun setScaledImages(view: View) {
		val accuracy: String = perceptualHash.getAccuracyByApiLevel().toString()
		view.findViewById<TextView>(R.id.tvStepTwo).text =
			resources.getString(
				R.string.show_compare_process_scaled_image_step_second,
				accuracy,
				accuracy
			)
		scaledImageOne = perceptualHash.getScaledBitmap(srcImageOne)
		scaledImageTwo = perceptualHash.getScaledBitmap(srcImageTwo)
		view.findViewById<ImageView>(R.id.ivScaledImageOne)
			.setImageBitmap(scaledImageOne)
		view.findViewById<ImageView>(R.id.ivScaledImageTwo)
			.setImageBitmap(scaledImageTwo)
	}

	private fun setupBackActions(view: View) {
		view.findViewById<ImageView>(R.id.ivArrowBack).setOnClickListener { close() }
		view.findViewById<TextView>(R.id.btnClose).setOnClickListener { close() }
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