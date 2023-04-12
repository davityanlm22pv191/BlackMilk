package com.example.diplom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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