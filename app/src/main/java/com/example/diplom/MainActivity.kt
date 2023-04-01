package com.example.diplom

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

	private companion object {
		const val PICK_IMAGE_ONE = 1
		const val PICK_IMAGE_TWO = 2
	}

	private var lastSelectedImageOneBitmap: Bitmap? = null
	private var lastSelectedImageTwoBitmap: Bitmap? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val btnGalleryOne = findViewById<TextView>(R.id.btnChooseOneFromGallery)
		val btnGalleryTwo = findViewById<TextView>(R.id.btnChooseTwoFromGallery)
		val btnLinkOne = findViewById<TextView>(R.id.btnChooseOneFromLink)
		btnLinkOne.setOnClickListener {
			this.loadImageOneFromLink() }
		btnGalleryOne.setOnClickListener { this.setImageOneFromGallery() }
		btnGalleryTwo.setOnClickListener { this.setImageTwoFromGallery() }
	}

	// Обрабатываем результат выбора изображения
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		(data != null && data.data != null).let {
			// Получаем URI выбранного изображения
			val imageUri = data?.data

			// Преобразуем URI в Bitmap
			val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

			if (requestCode == PICK_IMAGE_ONE && resultCode == RESULT_OK) {
				lastSelectedImageOneBitmap = bitmap
				findViewById<ImageView>(R.id.ivCurrentImageOne).setImageBitmap(bitmap)
			} else if (requestCode == PICK_IMAGE_TWO && resultCode == RESULT_OK) {
				lastSelectedImageTwoBitmap = bitmap
				findViewById<ImageView>(R.id.ivCurrentImageTwo).setImageBitmap(bitmap)
			}
		}
	}

	private fun loadImageOneFromLink() {
		// хард код чтобы получить лого google
//		Glide.with(this)
//			.asBitmap()
//			.load("https://www.google.ru/images/branding/googlelogo/2x/googlelogo_light_color_92x30dp.png")
//			.into(findViewById<ImageView>(R.id.ivCurrentImageOne))
	}

	private fun setImageOneFromGallery() {
		// Создаем Intent для открытия галереи
		val intent = Intent()
		intent.type = "image/*"
		intent.action = Intent.ACTION_GET_CONTENT

		// Запускаем Intent и ждем результат
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_ONE)
	}

	private fun setImageTwoFromGallery() {
		// Создаем Intent для открытия галереи
		val intent = Intent()
		intent.type = "image/*"
		intent.action = Intent.ACTION_GET_CONTENT

		// Запускаем Intent и ждем результат
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_TWO)
	}
}