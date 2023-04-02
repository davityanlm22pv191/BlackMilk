package com.example.diplom.fragment.chooseimagefromlink

import android.view.View
import android.widget.EditText

interface ChooseImageFromLinkContract {

	fun getEditTextString(view: View): String

	fun clearTextEdit(editText: EditText)

	fun close()

	fun onBackClicked()

	fun onEnterClicked(view: View)

	fun setupToolbar(view: View)

}