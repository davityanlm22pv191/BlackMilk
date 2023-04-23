package com.example.diplom.fragment.chooseimagefromlink

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.diplom.R
import com.example.diplom.fragment.home.HomeFragment
import com.example.diplom.fragment.home.callback.HomeCallback

class ChooseImageFromLinkFragment(
	parentFragment: HomeFragment,
	private val bitmapImage: Bitmap?,
	private val link: String?,
	private val imageNumber: Int,
) : Fragment(R.layout.fragment_choose_image_from_link),
	ChooseImageFromLinkContract {

	private var lastCorrectLink: String? = null

	// region ==================== Object creation ====================

	companion object {
		fun newInstance(
			parentFragment: HomeFragment,
			bitmapImage: Bitmap?,
			link: String?,
			imageNumber: Int
		): ChooseImageFromLinkFragment {
			val args = Bundle()
			val fragment =
				ChooseImageFromLinkFragment(parentFragment, bitmapImage, link, imageNumber)
			fragment.arguments = args
			return fragment
		}
	}

	// endregion

	// region ==================== Lifecycle ====================

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

	// region ==================== ChooseImageFromLinkContract ====================

	override fun clearTextEdit(editText: EditText) {
		editText.text.clear()
	}

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

	override fun onAgreeClicked(view: View) {
		val callBack = parentFragment as? HomeCallback

		val imageView = view.findViewById<ImageView>(R.id.ivImageFromLink)
		imageView.setBackgroundResource(0)
		imageView.isDrawingCacheEnabled = true
		val bitmapSrc = Bitmap.createBitmap(imageView.drawingCache)
		imageView.isDrawingCacheEnabled = false

		bitmapSrc?.let { bitmap ->
			lastCorrectLink?.let { link ->
				callBack?.setImageFromLink(bitmap, link, imageNumber)
			}
		}
	}

	override fun setupToolbar(view: View) {
		view.findViewById<TextView>(R.id.tvToolbarTitle).text =
			context?.getString(R.string.choose_image_from_link_title, imageNumber.toString())
	}

	override fun getEditTextString(view: View): String {
		return view.findViewById<EditText>(R.id.editTextLink).text.toString()
	}

	override fun onEnterClicked(view: View) {
		view.apply {
			val imageView = findViewById<ImageView>(R.id.ivImageFromLink)
			val link = getEditTextString(this)
			if (link.isNotBlank()) {
				Glide.with(this).clear(imageView)
				Glide.with(this)
					.load(link)
					.listener(object : RequestListener<Drawable> {
						override fun onLoadFailed(
							e: GlideException?,
							model: Any?,
							target: Target<Drawable>?,
							isFirstResource: Boolean
						): Boolean {
							setViewLoadError(view)
							lastCorrectLink = null
							return false
						}

						override fun onResourceReady(
							resource: Drawable?,
							model: Any?,
							target: Target<Drawable>?,
							dataSource: DataSource?,
							isFirstResource: Boolean
						): Boolean {
							setViewLoadSuccess(view)
							lastCorrectLink = link
							return false
						}
					})
					.into(imageView)
			} else {
				lastCorrectLink = null
				Glide.with(this).clear(imageView)
				findViewById<TextView>(R.id.tvImageFromLinkStateInfo)
					.text = context.getString(R.string.choose_image_from_link_wait_your_link)
			}
		}
		val inputMethodManager =
			requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		inputMethodManager.hideSoftInputFromWindow(
			view.findViewById<EditText>(R.id.editTextLink)?.windowToken,
			0
		)
	}

	// endregion

	//region ==================== Internal ====================

	private fun initUI(view: View) {
		view.apply {
			setupToolbar(view)

			if (link != null && bitmapImage != null) {
				setViewWithParameters(this)
			} else {
				setViewWithoutParameters(this)
			}

			onBackClicked()
			setOnClickListener(null)
			findViewById<ImageView>(R.id.btnEnter).setOnClickListener { onEnterClicked(this) }
			findViewById<ImageView>(R.id.ivArrowBack).setOnClickListener { close() }
			findViewById<TextView>(R.id.btnClearLink).setOnClickListener {
				clearTextEdit(findViewById(R.id.editTextLink))
				onEnterClicked(this)
			}
			findViewById<TextView>(R.id.btnAgree).setOnClickListener { onAgreeClicked(this) }
		}
	}

	private fun setViewWithoutParameters(view: View) {
		clearTextEdit(view.findViewById(R.id.editTextLink))
		view.findViewById<ImageView>(R.id.tvImageFromLinkStateInfo).isVisible = true
		view.findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = false
		view.findViewById<TextView>(R.id.tvIsYourImageMsg).isVisible = false
	}

	private fun setViewWithParameters(view: View) {
		view.findViewById<ImageView>(R.id.ivImageFromLink).setImageBitmap(bitmapImage)
		view.findViewById<EditText>(R.id.editTextLink).setText(link)
		view.findViewById<ImageView>(R.id.tvImageFromLinkStateInfo).isVisible = false
		view.findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = true
		view.findViewById<TextView>(R.id.tvIsYourImageMsg).isVisible = true
	}

	private fun setViewLoadError(view: View) {
		view.apply {
			findViewById<TextView>(R.id.tvImageFromLinkStateInfo)
				.text = context?.getText(R.string.choose_image_from_link_load_error_msg)
			findViewById<TextView>(R.id.tvIsYourImageMsg).isVisible = false
			findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = false
		}
	}

	private fun setViewLoadSuccess(view: View) {
		view.apply {
			val isYourMsg = findViewById<TextView>(R.id.tvIsYourImageMsg)
			isYourMsg.text = context?.getText(R.string.choose_image_from_link_is_your_image)
			isYourMsg.isVisible = true
			findViewById<TextView>(R.id.tvImageFromLinkStateInfo).text = ""
			findViewById<LinearLayout>(R.id.llAgreeOrClearButtons).isVisible = true
		}
	}

	// endregion
}