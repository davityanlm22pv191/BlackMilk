package com.example.diplom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diplom.fragment.home.HomeFragment

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val homeFragment = HomeFragment()
		supportFragmentManager.beginTransaction()
			.replace(R.id.rootElement, homeFragment)
			.commit()
	}
}