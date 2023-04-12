package com.example.diplom.fragment.showcompareprocess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.diplom.R
import com.example.diplom.fragment.home.HomeFragment
import com.example.diplom.fragment.showcompareprocess.model.ShowCompareProcessParams

class ShowCompareProcessFragment(
	parentFragment: HomeFragment,
	private val showCompareProcessParams: ShowCompareProcessParams,
) : Fragment(R.layout.fragment_show_compare_proccess), ShowCompareProcessContract {

	//region ==================== Object creation ====================

	companion object {
		fun newInstance(
			parentFragment: HomeFragment,
			showCompareProcessParams: ShowCompareProcessParams,
		): ShowCompareProcessFragment {
			val args = Bundle()
			val fragment = ShowCompareProcessFragment(parentFragment, showCompareProcessParams)
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
			setImagesSources(this)
			findViewById<ImageView>(R.id.ivArrowBack).setOnClickListener { close() }
			onBackClicked()
		}
	}

	private fun setImagesSources(view: View) {
		view.findViewById<ImageView>(R.id.ivSourceImageOne)
			.setImageBitmap(showCompareProcessParams.imageBitmapOne)
		view.findViewById<ImageView>(R.id.ivSourceImageTwo)
			.setImageBitmap(showCompareProcessParams.imageBitmapTwo)
	}

	// endregion
}