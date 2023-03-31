package com.example.diplom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
	// val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
	private companion object {
		const val PICK_IMAGE_REQUEST = 1
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val PICK_IMAGE_REQUEST = 1

		// Создаем Intent для открытия галереи
		val intent = Intent()
		intent.type = "image/*"
		intent.action = Intent.ACTION_GET_CONTENT

		// Запускаем Intent и ждем результат
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
	}

	// Обрабатываем результат выбора изображения
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
			// Получаем URI выбранного изображения
			val imageUri = data.data

			// Преобразуем URI в Bitmap
			val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

			// Используем выбранное изображение
//			val ivPhoto = findViewById<ImageView>(R.id.ivPhotoFromGallery)
//			ivPhoto.setImageBitmap(bitmap)
		}
	}
}