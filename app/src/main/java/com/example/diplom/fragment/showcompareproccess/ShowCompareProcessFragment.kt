package com.example.diplom.fragment.showcompareproccess

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.diplom.fragment.home.HomeFragment
import com.example.diplom.fragment.showcompareproccess.model.ShowCompareProcessParams

class ShowCompareProcessFragment(
	parentFragment: HomeFragment,
	params: ShowCompareProcessParams,
) : Fragment() {

	//region ==================== Object creation ====================

	companion object {
		fun newInstance(
			parentFragment: HomeFragment,
			params: ShowCompareProcessParams,
		): ShowCompareProcessFragment {
			val args = Bundle()
			val fragment = ShowCompareProcessFragment(parentFragment, params)
			fragment.arguments = args
			return fragment
		}
	}

	// endregion


}