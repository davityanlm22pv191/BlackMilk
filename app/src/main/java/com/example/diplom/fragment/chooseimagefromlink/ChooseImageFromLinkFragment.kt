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
	val bitmapImage: Bitmap?,
	val link: String?,
) : Fragment(R.layout.choose_image_from_link),
	ChooseImageFromLinkContract {

	//region ==================== Lifecycle ====================

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.choose_image_from_link, container, false)
		initUI(view)

		return view
	}

	// endregion

	//region ==================== ChooseImageFromLinkContract ====================

	override fun clearTextEdit(editText: EditText) {
		editText.text.clear()
	}

	// endregion

	//region ==================== Internal ====================

	private fun initUI(view: View) {
		view.apply {
			this.setOnClickListener(null)

			if (link != null && bitmapImage != null) {
				findViewById<ImageView>(R.id.ivImageFromLink).setImageBitmap(bitmapImage)
				findViewById<EditText>(R.id.editTextLink).setText(link)
				findViewById<ImageView>(R.id.tvImageFromLinkStateInfo).isVisible = false
				findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = true
				findViewById<TextView>(R.id.tvIsYourImageMsg).isVisible = true
			}
			findViewById<ImageView>(R.id.tvImageFromLinkStateInfo).isVisible = true
			findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = false
			findViewById<TextView>(R.id.tvIsYourImageMsg).isVisible = false
			findViewById<EditText>(R.id.editTextLink).apply {
				setOnClickListener { clearTextEdit(this) }
			}
		}
	}

	// endregion
}