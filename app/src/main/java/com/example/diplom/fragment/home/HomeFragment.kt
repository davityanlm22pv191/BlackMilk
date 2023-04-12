package com.example.diplom.fragment.home

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.diplom.R
import com.example.diplom.entity.PerceptualHashCompareImages
import com.example.diplom.fragment.chooseimagefromlink.ChooseImageFromLinkFragment
import com.example.diplom.fragment.home.callback.HomeCallback
import com.example.diplom.fragment.home.model.CurrentImage
import com.google.android.material.switchmaterial.SwitchMaterial

class HomeFragment : Fragment(), HomeContract, HomeCallback {

	private var imageOne: CurrentImage = CurrentImage(null, null)
	private var imageTwo: CurrentImage = CurrentImage(null, null)

	companion object {
		const val LOAD_IMAGE_FROM_LINK_FRAGMENT = "LOAD_IMAGE_FROM_LINK_FRAGMENT"
		private const val PICK_IMAGE_ONE = 1
		private const val PICK_IMAGE_TWO = 2
	}

	// region ==================== Lifecycle ====================

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_home, container, false)
		initUI(view)

		return view
	}

	// endregion

	// region ==================== Override ====================

	// Обрабатываем результат выбора изображения
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		(data != null && data.data != null).let {
			// Получаем URI выбранного изображения
			val imageUri = data?.data

			// Преобразуем URI в Bitmap
			val bitmap =
				MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)

			when {
				requestCode == PICK_IMAGE_ONE && resultCode == AppCompatActivity.RESULT_OK -> {
					view?.findViewById<TextView>(R.id.tvImageOneNotSelectedMsg)?.isVisible = false
					imageOne.bitmap = bitmap
					view?.findViewById<ImageView>(R.id.ivCurrentImageOne)?.setImageBitmap(bitmap)
				}
				requestCode == PICK_IMAGE_TWO && resultCode == AppCompatActivity.RESULT_OK -> {
					view?.findViewById<TextView>(R.id.tvImageTwoNotSelectedMsg)?.isVisible = false
					imageTwo.bitmap = bitmap
					view?.findViewById<ImageView>(R.id.ivCurrentImageTwo)?.setImageBitmap(bitmap)
				}
				else -> {}
			}
		}
		checkToReadyCompare()
	}

	// endregion

	// region ==================== HomeCallback ====================

	override fun onDataReceived(bitmapImage: Bitmap, link: String, imageNumber: Int) {
		when (imageNumber) {
			PICK_IMAGE_ONE -> {
				this.view?.findViewById<TextView>(R.id.tvImageOneNotSelectedMsg)?.isVisible = false
				imageOne.bitmap = bitmapImage
				this.view?.findViewById<ImageView>(R.id.ivCurrentImageOne)
					?.setImageBitmap(imageOne.bitmap)
				imageOne.link = link
			}
			PICK_IMAGE_TWO -> {
				this.view?.findViewById<TextView>(R.id.tvImageTwoNotSelectedMsg)?.isVisible = false
				imageTwo.bitmap = bitmapImage
				this.view?.findViewById<ImageView>(R.id.ivCurrentImageTwo)
					?.setImageBitmap(imageTwo.bitmap)
				imageTwo.link = link
			}
		}
		val fragment = childFragmentManager.findFragmentByTag(LOAD_IMAGE_FROM_LINK_FRAGMENT)
		if (fragment != null) {
			childFragmentManager.beginTransaction().remove(fragment).commit()
		}
		checkToReadyCompare()
	}

	// endregion

	// region ==================== HomeContract ====================

	override fun navigateToChooseImageFromLink(imageNumber: Int) {
		val fragment: Fragment = if (imageNumber == PICK_IMAGE_ONE) {
			ChooseImageFromLinkFragment.newInstance(
				this,
				imageOne.bitmap,
				imageOne.link,
				imageNumber
			)
		} else {
			ChooseImageFromLinkFragment.newInstance(
				this,
				imageTwo.bitmap,
				imageTwo.link,
				imageNumber
			)
		}

		childFragmentManager.beginTransaction()
			.add(R.id.rootElement, fragment, LOAD_IMAGE_FROM_LINK_FRAGMENT)
			.commitNow()
	}

	override fun setImageOneFromGallery() {
		// Создаем Intent для открытия галереи
		val intent = Intent()
		intent.type = "image/*"
		intent.action = Intent.ACTION_GET_CONTENT

		// Запускаем Intent и ждем результат
		startActivityForResult(
			Intent.createChooser(intent, "Select Picture"),
			PICK_IMAGE_ONE
		)
	}

	override fun setImageTwoFromGallery() {
		// Создаем Intent для открытия галереи
		val intent = Intent()
		intent.type = "image/*"
		intent.action = Intent.ACTION_GET_CONTENT

		// Запускаем Intent и ждем результат
		startActivityForResult(
			Intent.createChooser(intent, "Select Picture"),
			PICK_IMAGE_TWO
		)
	}

	override fun isAllImageLoaded(): Boolean {
		if (imageOne.bitmap != null && imageTwo.bitmap != null) {
			return true
		}
		return false
	}

	override fun checkToReadyCompare() {
		if (isAllImageLoaded()) {
			this.view?.findViewById<TextView>(R.id.btnCompare)?.isEnabled = true
			this.view?.findViewById<SwitchMaterial>(R.id.switchShowCompare)?.isVisible = true
		}
	}

	override fun onBtnCompareClicked() {
		val compare = PerceptualHashCompareImages()
		this.imageOne.bitmap?.let {
			this.view?.findViewById<ImageView>(R.id.ivCurrentImageOne)?.setImageBitmap(compare.getScaledBitmap(it))
		}
	}

	// endregion

	// region ==================== Internal ====================

	private fun initUI(view: View) {
		setClickListeners(view)
	}

	private fun setClickListeners(view: View) {
		view.apply {
			val btnGalleryOne = findViewById<TextView>(R.id.btnChooseOneFromGallery)
			val btnGalleryTwo = findViewById<TextView>(R.id.btnChooseTwoFromGallery)
			val btnLinkOne = findViewById<TextView>(R.id.btnChooseOneFromLink)
			val btnLinkTwo = findViewById<TextView>(R.id.btnChooseTwoFromLink)
			val btnCompare = findViewById<TextView>(R.id.btnCompare)
			btnLinkOne.setOnClickListener { navigateToChooseImageFromLink(PICK_IMAGE_ONE) }
			btnLinkTwo.setOnClickListener { navigateToChooseImageFromLink(PICK_IMAGE_TWO) }
			btnGalleryOne.setOnClickListener { setImageOneFromGallery() }
			btnGalleryTwo.setOnClickListener { setImageTwoFromGallery() }
			btnCompare.setOnClickListener { onBtnCompareClicked() }
		}
	}

	// endregion
}