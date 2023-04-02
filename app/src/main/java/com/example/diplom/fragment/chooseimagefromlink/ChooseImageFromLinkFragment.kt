package com.example.diplom.fragment.chooseimagefromlink

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.diplom.R

class ChooseImageFromLinkFragment(
	private val bitmapImage: Bitmap?,
	private val link: String?,
) : Fragment(R.layout.fragment_choose_image_from_link),
	ChooseImageFromLinkContract {

	companion object {
		fun newInstance(bitmapImage: Bitmap?, link: String?): ChooseImageFromLinkFragment {
			val args = Bundle()
			val fragment = ChooseImageFromLinkFragment(bitmapImage, link)
			fragment.arguments = args
			return fragment
		}
	}

	//region ==================== Lifecycle ====================

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_choose_image_from_link, container, false)
		initUI(view)

		return view
	}

	// endregion

	//region ==================== ChooseImageFromLinkContract ====================

	override fun clearTextEdit(editText: EditText) {
		editText.text.clear()
	}

	override fun close() {
		parentFragmentManager.beginTransaction()
			.remove(this)
			.commit()
	}

	// endregion

	//region ==================== Internal ====================

	private fun initUI(view: View) {
		view.apply {
			setupEditText(this)

			if (link != null && bitmapImage != null) {
				setViewWithParameters(this)
			} else {
				setViewWithoutParameters(this)
			}

			onBackClicked()
			setOnClickListener(null)
			findViewById<ImageView>(R.id.ivArrowBack).setOnClickListener { close() }
		}
	}

	private fun setupEditText(view: View) {
		view.findViewById<EditText>(R.id.editTextLink).apply {

		}
	}

	private fun onBackClicked() {
		requireActivity().onBackPressedDispatcher.addCallback(
			viewLifecycleOwner,
			object : OnBackPressedCallback(true) {
				override fun handleOnBackPressed() {
					close()
				}
			})
	}

	private fun setViewWithoutParameters(view: View) {
		view.findViewById<ImageView>(R.id.tvImageFromLinkStateInfo).isVisible = true
		view.findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = false
		view.findViewById<TextView>(R.id.tvIsYourImageMsg).isVisible = false
		view.findViewById<EditText>(R.id.editTextLink).apply {
			setOnClickListener { clearTextEdit(this) }
		}
	}

	private fun setViewWithParameters(view: View) {
		view.findViewById<ImageView>(R.id.ivImageFromLink).setImageBitmap(bitmapImage)
		view.findViewById<EditText>(R.id.editTextLink).setText(link)
		view.findViewById<ImageView>(R.id.tvImageFromLinkStateInfo).isVisible = false
		view.findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = true
		view.findViewById<TextView>(R.id.tvIsYourImageMsg).isVisible = true
	}

	// endregion
}